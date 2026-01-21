package auxiliar;

public class NoHayFicherosException extends Exception {
    private static final long serialVersionUID = 1L;

    public NoHayFicherosException(String string) {
        super(string);
    }
}
