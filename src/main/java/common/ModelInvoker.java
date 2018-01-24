package common;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.ModelEvaluator;
import org.jpmml.evaluator.ModelEvaluatorFactory;
import org.jpmml.evaluator.TargetField;
import org.jpmml.model.PMMLUtil;
import org.xml.sax.SAXException;

public class ModelInvoker {
    private final ModelEvaluator modelEvaluator;

    public ModelInvoker(String pmmlFileName) {
        PMML pmml = null;
        InputStream is = null;

        try {
            if (pmmlFileName != null) {
                is = ModelInvoker.class.getClassLoader().getResourceAsStream(pmmlFileName);
                pmml = PMMLUtil.unmarshal(is);
            }
        } catch (SAXException var15) {
            pmml = null;
        } catch (JAXBException var16) {
            pmml = null;
        } finally {
            try {
                is.close();
            } catch (IOException var14) {
                ;
            }

        }

        this.modelEvaluator = ModelEvaluatorFactory.newInstance().newModelEvaluator(pmml);
        this.modelEvaluator.verify();
    }

    public ModelInvoker(InputStream is) {
        PMML pmml = null;

        try {
            pmml = PMMLUtil.unmarshal(is);
        } catch (SAXException var14) {
            pmml = null;
        } catch (JAXBException var15) {
            pmml = null;
        } finally {
            try {
                is.close();
            } catch (IOException var13) {
                ;
            }

        }

        this.modelEvaluator = ModelEvaluatorFactory.newInstance().newModelEvaluator(pmml);
        this.modelEvaluator.verify();
    }

    public Map<FieldName, ?> invoke(Map<FieldName, Object> paramsMap) {
        return this.modelEvaluator.evaluate(paramsMap);
    }

    public List<TargetField> getTargetFields() {
        return this.modelEvaluator.getTargetFields();
    }
}
