import scanner.IScanner;
import scanner.StdInputScanner;
import scanner.Token;
import scanner.Tokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Interpreter {

    public static void main(String[] args) {
        try {
            IScanner scanner = StdInputScanner.getStdInputScanner();
            Tokenizer tokenizer = new Tokenizer();

            ArrayList<Token> literals = new ArrayList<>();
            ArrayList<Token> numbers = new ArrayList<>();
            ArrayList<Token> lpars = new ArrayList<>();
            ArrayList<Token> rpars = new ArrayList<>();


            while(true) {
                Token token = tokenizer.getNextToken(scanner);
                if(token.equals(Token.EOF_TOKEN)) break;
                switch(token.getTokenKind()) {
                    case LiteralAtom:
                        literals.add(token);
                        break;
                    case NumericAtom:
                        numbers.add(token);
                        break;
                    case LPar:
                        lpars.add(token);
                        break;
                    case RPar:
                        rpars.add(token);
                        break;
                    case Error:
                        System.out.println(token);
                        System.exit(-1);
                        break;
                }

            }
            String all_lexmes = String.join(", ", literals.stream().map(t -> t.getLexme()).collect(Collectors.toList()));
            long sum = numbers.stream().map(t -> t.getLexme()).map(Long::parseLong).reduce((x, y)->x+y).orElse(0L);
            System.out.printf("LITERAL ATOMS: %d, %s\n", literals.size(), all_lexmes);
            System.out.printf("NUMERIC ATOMS: %d, %d\n", numbers.size(), sum);
            System.out.printf("OPEN PARENTHESES: %d\n", lpars.size());
            System.out.printf("CLOSING PARENTHESES: %d\n", rpars.size());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while reading standard input.");
        }
    }
}
