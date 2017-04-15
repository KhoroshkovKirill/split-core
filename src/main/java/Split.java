import java.io.*;

/**
 * Created by khoroshkovkirill on 01.04.17.
 */
final class Split {
    final private boolean numberFormat;
    final private boolean sizeInLines;//\
    final private boolean sizeInChars;//-одно из трех(или не одного)
    final private boolean countOfFiles;//
    final private int size;
    final private boolean ofile;
    final private String nameOfOutFiles;
    final private String nameOfInFile;

    public Split(boolean numberFormat, boolean sizeInLines, boolean sizeInChars, boolean countOfFiles,
                 int size, boolean ofile, String nameOfOutFiles, String nameOfInFile) {
        if (sizeInChars && sizeInLines || sizeInChars && countOfFiles || sizeInLines && countOfFiles){
            throw new IllegalArgumentException("Недопустимое сочетание флагов управления размером");
        }
        if ((sizeInChars || sizeInLines || countOfFiles) == (size == 0)){
            throw new IllegalArgumentException("Указан размер но не указан флаг управления размером(или наоборот)");
        }
        if (ofile == (nameOfOutFiles == null)){
            throw new IllegalArgumentException("Отсутствует флаг -o, но указано имя выходного файла(или наоборот)");
        }
        this.numberFormat = numberFormat;
        this.sizeInLines = sizeInLines;
        this.sizeInChars = sizeInChars;
        this.countOfFiles = countOfFiles;
        this.size = size;
        this.ofile = ofile;
        this.nameOfOutFiles = nameOfOutFiles;
        this.nameOfInFile = nameOfInFile;
    }

    public void writeInFiles() throws IOException{
        //Название выходных файлов
        String out;
        if (!this.ofile){
            out = "x";
        }
        else{
            if (this.nameOfOutFiles.equals("-")){
                out = this.nameOfInFile;
            }
            else{
                out = this.nameOfOutFiles;
            }
        }
        //Чтение и запись
        if (this.sizeInLines) writeInFilesIfSizeInLines(out, this.size);
        if (this.countOfFiles) writeInFilesIfSizeInChars(out, this.lengthOfFile(nameOfInFile) / this.size + 1);
        if (this.sizeInChars) writeInFilesIfSizeInChars(out, this.size);
        if (this.size == 0) writeInFilesIfSizeInLines(out, 100);
    }

    public void writeInFilesIfSizeInLines(String out, int size) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(this.nameOfInFile));
        String str;
        int number = 0;
        Integer numFile = 0;
        BufferedWriter bw = new BufferedWriter(new FileWriter(out + "1" + ".txt"));
        while ((str = br.readLine()) != null) {
            if (numFile < number / size + 1) {
                numFile = number / size + 1;
                bw.close();
                bw = new BufferedWriter(new FileWriter(out + numFile.toString() + ".txt"));
            }
            else{
                bw.newLine();
            }
            bw.write(str);
            number++;
        }
        bw.close();
    }

    public void writeInFilesIfSizeInChars(String out, int size) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(this.nameOfInFile));
        int ch;
        int number = 0;
        Integer numFile = 0;
        BufferedWriter bw = new BufferedWriter(new FileWriter(out + "1" + ".txt"));
        while ((ch = br.read()) != -1) {
            if (numFile < number / size + 1) {
                numFile = number / size + 1;
                bw.close();
                bw = new BufferedWriter(new FileWriter(out + numFile.toString() + ".txt"));
            }
            bw.write(ch);
            number++;
        }
        bw.close();
    }

    public int lengthOfFile(String nameOfFile) throws IOException{
        int length = 0;
        BufferedReader br = new BufferedReader (new FileReader(nameOfFile));
        while(br.read()!=-1){
            length++;
        }
        return length;
    }
}