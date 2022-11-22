package service;


import java.util.List;
import java.util.Vector;

import model.FixedObject;
import model.Product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Fabian Moos
 */
public class JsonInput implements Input {

    private int width = -1;
    private int height = -1;
    private int turns = -1;
    private int time = -1;
    private final List<FixedObject> objects = new Vector<>();
    private final List<Product> products = new Vector<>();

    /**
     * @param inputFile
     */
    public JsonInput(String inputFile) {
        var inputString = JsonInput.readInputFile(inputFile);
        var tokens = JsonInput.tokenize(inputString);

        for (int i = 0; i < tokens.length; ++i) {
            System.out.print(tokens[i] + " ");
        }
        System.out.println();

        var it = Arrays.stream(tokens).iterator();
        assert (it.next().equals("{"));
        while (it.hasNext()) {
            var token = it.next();
            switch (token) {
                case "width" -> this.width = Integer.parseInt(it.next());
                case "height" -> this.height = Integer.parseInt(it.next());
                case "turns" -> this.turns = Integer.parseInt(it.next());
                case "time" -> this.time = Integer.parseInt(it.next());
                case "objects" -> {
                    assert (it.next().equals("["));
                    while (it.hasNext() && !token.equals("]")) {
                        token = it.next();
                    }
                }
                case "products" -> {
                    assert (it.next().equals("["));
                    while (it.hasNext() && !token.equals("]")) {
                        token = it.next();
                    }
                }
                default -> System.err.println(">>> UNEXPECTED TOKEN IN JSON FILE!");
            }
        }
//    System.out.println("Input i: width = " + this.width + "; height = " + this.height);
//    System.out.println("         turns = " + this.turns + "; time = " + this.time);
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public List<FixedObject> getInputObjects() {
        return this.objects;
    }

    @Override
    public List<Product> getProducts() {
        return this.products;
    }

    @Override
    public int getTurns() {
        return this.turns;
    }

    @Override
    public int getTime() {
        return this.time;
    }

    /**
     * @param inputFileName The absolute path or the relative path to the current working directory of
     *                      the input file.
     * @return The file inputFileName as one String. An empty String if an Exception has been caused.
     */
    private static String readInputFile(String inputFileName) {
        try {
            BufferedReader inputReader = new BufferedReader(new FileReader(inputFileName));
            String inputString = inputReader.lines().collect(Collectors.joining());
            inputReader.close();
            return inputString;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.err.println(Arrays.toString(e.getStackTrace()));
        }
        return "";
    }

    /**
     * @param fileString
     * @return
     */
    private static String[] tokenize(String fileString) {
        return Arrays.stream(fileString.replace("{", ":{:")
                .replace("}", ":}:")
                .replace("[", ":[:")
                .replace("]", ":]:")
                .split(",|:|\"|\"")).filter((str) -> !str.isEmpty()).toArray(String[]::new);
    }
}
