import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.IOException;

public class Main {

    @Argument(required = true,usage = "Command")
    private String command;

    @Option(name = "-d",usage = "Выходные файлы следует называть “ofile1, ofile2, ofile3, ofile4 …”")
    private boolean numberFormat;

    @Option(name = "-l",usage = "Размер выходных файлов в строчках")
    private int sizeInLines;

    @Option(name = "-c",usage = "Размер выходных файлов в символах")
    private int sizeInChars;

    @Option(name = "-n",usage = "Количество выходных файлов")
    private int countOfFiles;

    @Option(name = "-o",usage = "Задаёт базовое имя выходного файла")
    private String nameOfOutFiles;

    @Argument(required = true, index = 1, usage = "Имя входного файла")
    private String nameOfInFile;

    public static void main(String[] args) throws IOException {
        try {
            new Main().doSplit(args);
            System.exit(0);
        }
        catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
            System.exit(65);
        }
        catch (IOException ex){
            System.out.println("Ошибка чтения/записи файла: " + ex.getLocalizedMessage() + " (IOException)");
            System.exit(74);
        }
        catch (CmdLineException ex){
            System.out.println("Ошибка чтения командной строки: " + ex.getLocalizedMessage() + " (CmdLineException)");
            System.exit(64);
        }
        catch (Exception ex){
            System.out.println("Беда: " + ex.getLocalizedMessage() + " (Exception)");
            System.exit(-1);
        }
    }

    private void doSplit(String[] args) throws CmdLineException, IOException {
        CmdLineParser parser = new CmdLineParser(this);
        parser.parseArgument(args);
        Split splitObj = new Split(numberFormat, sizeInLines, sizeInChars, countOfFiles, nameOfOutFiles, nameOfInFile);
        splitObj.writeInFiles();
    }

}