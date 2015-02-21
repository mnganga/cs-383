import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hduser on 2/21/15.
 */
public class Perceptron {

    // Sizing constants to allow for future enhancements
    private static final int NUM_TEMPLATES = 26;
    private static final int NUM_BITS_PER_ROW = 5;
    private static final int NUM_BITS_PER_COL = 5;

    // Perceptron contains an list of Letter objects called templates
    private List<Letter> templates = new ArrayList<Letter>();

    public int correlate(Letter template, Letter pattern) {

        return 0;
    }

    public int guess(Letter pattern) {

        return 0;
    }

    public int findCorrect(Letter pattern) {

        return 0;
    }

    public void checkLearn(int guess, int correct, Letter pattern) {

    }

    public void reward(int correctIndex, Letter pattern) {

    }

    public void punish(int correctIndex, Letter pattern) {

    }

    public void trainTemplates(File trainingFile) {

    }

    public void recognizeCharacters(File recognitionFile, File outputFile) {

    }

    public static void main(String[] argv) throws Exception {

    }
}
