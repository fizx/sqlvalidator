import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

public class App {
    static Throwable exception = null;

    public static void main(String[] args) throws IOException {
        String line = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while ((line = reader.readLine()) != null) {
            exception = null;
            SQLLexer lexer = new SQLLexer(new ANTLRInputStream(line));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            SQLParser parser = new SQLParser(tokens);
            BailErrorStrategy handler = new BailErrorStrategy();
            parser.setErrorHandler(handler);
            try {
                parser.sql();
            } catch (ParseCancellationException e) {
                try {
                    throw e.getCause();
                } catch (NoViableAltException inner) {
                    if (inner.getStartToken().getType() != Token.EOF) {
                        exception = inner;
                    }
                } catch (RecognitionException inner) {
                    if (inner.getOffendingToken().getType() != Token.EOF) {
                        exception = inner;
                    }
                } catch (Throwable throwable) {
                    exception = throwable;
                }
            }
            if (exception == null) {
                System.out.println("ok");
            } else {
                System.out.println("error");
            }
        }
    }
}
