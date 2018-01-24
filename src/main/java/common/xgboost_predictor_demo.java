package common;

import biz.k11i.xgboost.Predictor;
import biz.k11i.xgboost.util.FVec;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.AbstractMap.SimpleEntry;

public class xgboost_predictor_demo {

    public static void main(String[] args) throws java.io.IOException {

        simpleTest();
    }

    private static void simpleTest() throws java.io.IOException {

        String modelFilePath = "E:\\07_javaFile\\xgboost_demo\\data\\gaobao_test.model";
        // If you want to use faster exp() calculation, uncomment the line below
        // ObjFunction.useFastMathExp(true);
        Predictor predictor = new Predictor(new java.io.FileInputStream(modelFilePath));

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss");//设置日期格式
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间

        // Create feature vector from dense representation by array
//        double[] denseArray = getData();

        List<double[]> denseArrayList = getDataByExcel();
        for (double[] denseArray : denseArrayList) {
            doPredict(predictor, denseArray);
        }
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
    }

    private static void doPredict(Predictor predictor, double[] denseArray) {
        FVec fVecDense = FVec.Transformer.fromArray(
                denseArray,
                true /* treat zero element as N/A */);
        // Create feature vector from sparse representation by map
        FVec fVecSparse = FVec.Transformer.fromMap(new java.util.HashMap<Integer, Double>() {{
            put(2, 32.);
            put(5, 16.);
            put(6, -8.);
        }});

        // Predict probability or classification
        double[] prediction = predictor.predict(fVecDense);
        for (int i = 0; i < prediction.length; i++) {
//            System.out.println(prediction[i]);
        }

        // Predict leaf index of each tree
        // leafIndexes[i] has a leaf index of i-th tree
        int[] leafIndexes = predictor.predictLeaf(fVecDense);
        for (int i = 0; i < leafIndexes.length; i++) {
            //System.out.println(leafIndexes[i]);
        }
    }

    public static double[] getData() {
        double[] denseArray = new double[32];
        denseArray[0] = 2;
        Random r = new Random();
        for (int i = 1; i < denseArray.length; i++) {
            denseArray[i] = 100 * i;
        }
        return denseArray;
    }

    public static List<double[]> getDataByExcel() throws IOException {
        List<double[]> result = new ArrayList<>();

        File file = new File("E:\\07_javaFile\\xgboost_demo\\src\\main\\resources\\to_gb.xls");
        String[][] data = ExcelOperate.getExcelData(file, 1);
        int rowLength = data.length;
        for (int i = 0; i < rowLength; i++) {
            double[] denseArray = new double[data[i].length];
            for (int j = 0; j < data[i].length; j++) {
                //System.out.print(data[i][j] + "\t\t");
                String value = data[i][j];
                if (!value.isEmpty())
                    denseArray[j] = Double.parseDouble(value);
            }
            result.add(denseArray);
        }
        return result;
    }

}
