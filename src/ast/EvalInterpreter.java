package ast;

import parser.Parser;
import parser.ParsingException;
import scanner.IScanner;
import scanner.StdInputScanner;
import scanner.Tokenizer;
import scanner.TokenizerAdapter;

/**
 * Created by haoyu on 2/16/2016.
 */
public class EvalInterpreter {
    public static void main(String[] test) throws Exception {
        IScanner scanner = StdInputScanner.getStdInputScanner();
        Tokenizer tokenizer = new Tokenizer();
        Parser parser = new Parser(new TokenizerAdapter(scanner, tokenizer));
        Evaluator evaluator = new Evaluator();
        do {
            try {
                System.out.println(
                        evaluator.eval(parser.getNextExcepression()).toListNotation()
                );
            } catch (ParsingException pe) {
                System.out.println(pe.getMessage());
                break;
            } catch (EvaluationException pe) {
                System.out.println(pe.getMessage());
                break;
            }
        } while (parser.hasNext());
    }
}
