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
public class TypeCheckInterpreter {
    public static void main(String[] test) throws Exception {
        IScanner scanner = StdInputScanner.getStdInputScanner();
        Tokenizer tokenizer = new Tokenizer();
        Parser parser = new Parser(new TokenizerAdapter(scanner, tokenizer));
        Evaluator evaluator = new Evaluator();
        do {
            try {
                TreeNode exp = parser.getNextExcepression();
                evaluator.getType(exp);
                evaluator.getBound(exp);
                System.out.println( exp.toListNotation() );
            } catch (ParsingException pe) {
                System.out.println(pe.getMessage());
                break;
            } catch (TypeCheckException pe) {
                System.out.println(pe.getMessage());
                continue;
            }
        } while (parser.hasNext());
    }
}
