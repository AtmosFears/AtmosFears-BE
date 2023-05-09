package atmosfears.AtmosFearsBE.model;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Predictor {

    public static double[] predict(String csv_file_path){

        // TODO odpalanie wspólnego rCallera dla wszystkich wywołań predict'u

        // MODEL WILL NOT WORK IF THE NECESSARY R PACKAGES ARE NOT INSTALLED ALONGSIDE R ITSELF
        // For detail about how to install the packages refer to Rsetup.txt file in the project sources

        // EXAMPLE OF ARGUMENTS IN USE:
        // double[] pred = Predictor.predict("src/main/data/dane_sensora_ops.csv");

        try{

            // R ENVIRONMENT INITIALIZATION
            RCaller rCaller = RCaller.create();
            RCode code = RCode.create();

            // LOADING OF THE LIBRARIES
            code.addRCode("library(tidyverse)");
            code.addRCode("library(tidymodels)");
            code.addRCode("library(openair)");
            code.addRCode("tidymodels_prefer()");
            code.addRCode("library(\"Cubist\")");
            code.addRCode("library(\"reshape2\")");
            code.addRCode("library(\"plyr\")");
            code.addRCode("library(\"rules\")");
            code.addRCode("library(\"rio\")");
            code.addRCode("library(\"openxlsx\")");
            code.addRCode("library(\"zip\")");
            code.addRCode("conflicted::conflicts_prefer(plyr::mutate)");

            // LOADING OF MODEL
            code.addRCode("load(\"src/main/data/model.Rdata\")");

            // LOADING OF DATA
            code.addRCode("ops_data <- rio::import(file = \"" + csv_file_path + "\", setclass = \"tbl_df\")");
            code.addRCode("ops_data <- ops_data |> mutate(wday = as.factor(wday), hour = as.factor(hour))");

            // PERFORMING "CALIBRATION"
            code.addRCode("out <- bind_cols(ops_data, predict(model$.workflow[[1]], ops_data, type = \"numeric\"))");
            code.addRCode("out <- out |> select(.pred)");

            // RETRIEVING RESULT
            rCaller.setRCode(code);
            rCaller.runAndReturnResult("out");
            return rCaller.getParser().getAsDoubleArray("_pred");

        }
        catch (Exception e) {
            Logger.getLogger(examples.Main.class.getName()).log(Level.SEVERE, e.getMessage());
        }

        // RETURN -1 IN CASE OF ERROR
        return new double[]{-1};
    }

}
