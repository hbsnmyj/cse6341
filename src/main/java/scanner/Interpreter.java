package scanner;

import ast.Token;

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
                        System.out.printf("ERROR: " + token.toString() + "\n");
                        System.exit(-1);
                        break;
                }

            }
            String all_lexmes = String.join(", ", literals.stream().map(Token::getLexme).collect(Collectors.toList()));
            long sum = numbers.stream().map(Token::getLexme).map(Long::parseLong).reduce((x, y)->x+y).orElse(0L);
            System.out.printf("LITERAL ATOMS: %d", literals.size());
            if(literals.size() != 0)
                System.out.printf(", %s\n", all_lexmes);
            else
                System.out.printf("\n");
            System.out.printf("NUMERIC ATOMS: %d", numbers.size());
            if(numbers.size() != 0)
                System.out.printf(", %d\n", sum);
            else
                System.out.printf("\n");
            System.out.printf("OPEN PARENTHESES: %d\n", lpars.size());
            System.out.printf("CLOSING PARENTHESES: %d\n", rpars.size());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while reading standard input.");
        }
    }
}
