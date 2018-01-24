package common;


import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.TargetField;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ModelCalcConsole {
    public ModelCalcConsole() {
    }

    public static void main(String[] args) throws IOException {
        String modelArgsFilePath = "E:\\07_javaFile\\xgboost_demo\\src\\main\\resources\\to_gb.csv";
        String pmmlPath = "E:\\07_javaFile\\xgboost_demo\\data\\gaobao_test.pmml";

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss");//设置日期格式
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间

        ModelInvoker invoker = new ModelInvoker(new FileInputStream(pmmlPath));
        List<Map<FieldName, Object>> paramList = readInParams(modelArgsFilePath);
        int lineNum = 0;
        Iterator var6 = paramList.iterator();

        while (var6.hasNext()) {
            Map<FieldName, Object> param = (Map) var6.next();
            ++lineNum;
//            System.out.println("--------------------------------------- Num: " + lineNum + " -------------------------------------");

            Map<FieldName, ?> results = invoker.invoke(param);
            List<TargetField> targetFields = invoker.getTargetFields();
//            //获得结果，作为回归预测的例子，只有一个输出。对于分类问题等有多个输出。
//            for (TargetField targetField : targetFields) {
//                FieldName targetFieldName = targetField.getName();
//                Object targetFieldValue = results.get(targetFieldName);
//                System.out.println("target: " + targetFieldName.getValue() + " value: " + targetFieldValue);
//            }

            Set<FieldName> keySet = results.keySet();
            Iterator var10 = keySet.iterator();
            while (var10.hasNext()) {
                FieldName fn = (FieldName) var10.next();
                if (fn.getValue().equals("probability(1)")){
//                    System.out.println(results.get(fn).toString());
                }
            }

//            System.out.println("--------------------------------------- Num: " + lineNum + " -------------------------------------");
        }

        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
    }

    private static void test()
    {


    }

    private static List<Map<FieldName, Object>> readInParams(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String[] nameArr = br.readLine().split(",");
        List<Map<FieldName, Object>> list = new ArrayList();
        String paramLine = null;

        while ((paramLine = br.readLine()) != null) {
            Map<FieldName, Object> map = new HashMap();
            String[] paramLineArr = paramLine.split(",");

            for (int i = 0; i < paramLineArr.length; ++i) {
                map.put(new FieldName(nameArr[i]), paramLineArr[i]);
            }

            list.add(map);
        }

        return list;
    }
}