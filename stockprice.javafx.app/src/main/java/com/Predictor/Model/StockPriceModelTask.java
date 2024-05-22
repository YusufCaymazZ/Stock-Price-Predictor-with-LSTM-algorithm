package com.Predictor.Model;
import javafx.concurrent.Task;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.nn.conf.BackpropType;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.nd4j.linalg.learning.config.RmsProp;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerMinMaxScaler;
import org.datavec.api.writable.Writable;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StockPriceModelTask extends Task<Void> {
    private final List<Double> actualValues = new ArrayList<>();
    private final List<Double> predictedValues = new ArrayList<>();
    private final List<Double> futurePredictedValues = new ArrayList<>();

    public List<Double> getActualValues() {
        return actualValues;
    }

    public List<Double> getPredictedValues() {
        return predictedValues;
    }

    public List<Double> getFuturePredictedValues() {
        return futurePredictedValues;
    }

    @Override
    protected Void call() throws Exception {
        String filePath = "C:\\Users\\cayma\\OneDrive\\Masaüstü\\JavaProjeDatasets\\APPLE.csv";
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }

        RecordReader rr = new CSVRecordReader(1, ',');
        rr.initialize(new FileSplit(file));
        List<DataSet> dataSetList = new ArrayList<>();
        while (rr.hasNext()) {
            List<Writable> row = rr.next();
            double[] features = new double[4];
            double[] labels = new double[1];
            features[0] = Double.parseDouble(row.get(1).toString().replace("$", ""));
            features[1] = Double.parseDouble(row.get(3).toString().replace("$", ""));
            features[2] = Double.parseDouble(row.get(4).toString().replace("$", ""));
            features[3] = Double.parseDouble(row.get(5).toString().replace("$", ""));
            labels[0] = features[0];

            INDArray featureArray = Nd4j.create(features).reshape(1, 4, 1);
            INDArray labelArray = Nd4j.create(labels).reshape(1, 1, 1);
            dataSetList.add(new DataSet(featureArray, labelArray));
        }

        DataSetIterator dataSetIterator = new ListDataSetIterator<>(dataSetList);
        NormalizerMinMaxScaler normalizer = new NormalizerMinMaxScaler();
        normalizer.fit(dataSetIterator);
        dataSetIterator.reset();
        dataSetIterator.setPreProcessor(normalizer);

        int lstmLayerSize = 50;
        int tbpttLength = 20;

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
            .weightInit(WeightInit.XAVIER)
            .updater(new RmsProp(0.005))
            .list()
            .layer(0, new LSTM.Builder().nIn(4).nOut(lstmLayerSize).activation(Activation.TANH).build())
            .layer(1, new RnnOutputLayer.Builder(LossFunctions.LossFunction.MSE).activation(Activation.IDENTITY).nIn(lstmLayerSize).nOut(1).build())
            .backpropType(BackpropType.TruncatedBPTT).tBPTTForwardLength(tbpttLength).tBPTTBackwardLength(tbpttLength)
            .build();

        MultiLayerNetwork network = new MultiLayerNetwork(conf);
        network.init();
        network.setListeners(new ScoreIterationListener(10));
        network.fit(dataSetIterator, 64); //EPOCH

        dataSetIterator.reset();
        while (dataSetIterator.hasNext()) {
            DataSet t = dataSetIterator.next();
            INDArray features = t.getFeatures();
            INDArray labels = t.getLabels();
            INDArray predicted = network.output(features, false);
            actualValues.add(labels.getDouble(0));
            predictedValues.add(predicted.getDouble(0));
        }

        INDArray lastFeatures = dataSetList.get(dataSetList.size() - 1).getFeatures();
        for (int i = 0; i < 5; i++) {
            INDArray predicted = network.output(lastFeatures, false);
            futurePredictedValues.add(predicted.getDouble(0));
            INDArray newLastFeatures = Nd4j.create(new long[]{1, 4, 1});
            newLastFeatures.put(new INDArrayIndex[]{NDArrayIndex.all(), NDArrayIndex.interval(1, 4), NDArrayIndex.point(0)}, lastFeatures.get(NDArrayIndex.all(), NDArrayIndex.interval(1, 4), NDArrayIndex.all()));
            newLastFeatures.putScalar(new long[]{0, 3, 0}, predicted.getDouble(0));
            lastFeatures = newLastFeatures;
        }

        return null;
    }
}
