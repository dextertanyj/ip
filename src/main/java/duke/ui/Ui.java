package duke.ui;

public interface Ui {

    void print(String content);

    void greet();

    void showLoadingError();

    void exit();

    String readCommand();
}
