package parser;

import scanner.IScanner;
import scanner.StdInputScanner;
import scanner.Tokenizer;
import scanner.TokenizerAdapter;

/**
 * Created by snmyj on 1/26/16.
 */
public class Interpreter {
    public static void main(String[] test) throws Exception {
        IScanner scanner = StdInputScanner.getStdInputScanner();
        Tokenizer tokenizer = new Tokenizer();
        Parser parser = new Parser(new TokenizerAdapter(scanner, tokenizer));
        do {
            try {
                System.out.println(parser.getNextExcepression());
            } catch (ParsingException pe) {
                System.out.println(pe.getMessage());
                break;
            }
        } while (parser.hasNext());
    }
}
