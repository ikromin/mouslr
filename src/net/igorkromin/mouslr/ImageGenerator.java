package net.igorkromin.mouslr;

import java.io.File;
import java.io.IOException;

/**
 * Created by ikromin on 8/07/2015.
 */
public interface ImageGenerator {

    static final String MOUSLR_HEADER = "MOUSLR";

    void generate(File inputData, File outputFile) throws IOException;

}
