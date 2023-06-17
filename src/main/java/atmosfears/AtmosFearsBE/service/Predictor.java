package atmosfears.AtmosFearsBE.service;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Service
public class Predictor {

    private static final RCaller rCaller = RCaller.create();

    public List<Double> predict(String csv_file_path){
        // MODEL WILL NOT WORK IF THE NECESSARY R PACKAGES ARE NOT INSTALLED ALONGSIDE R ITSELF
        // For detail about how to install the packages refer to README

        // EXAMPLE OF ARGUMENTS IN USE:
        // Predictor predictor = new Predictor();
        // List<Double> pred = predictor.predict("src/main/data/example_sensor_data.csv");
        try{

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
            return DoubleStream.of( rCaller.getParser()
                                            .getAsDoubleArray("_pred") )
                                .boxed()
                                .collect(Collectors.toCollection(ArrayList::new));

        }
        catch (Exception e) {
            Logger.getLogger(examples.Main.class.getName())
                    .log(Level.SEVERE, e.getMessage());
        }

        return null;
    }

}
