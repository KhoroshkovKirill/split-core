import java.io.*;

/**
 * Created by khoroshkovkirill on 01.04.17.
 */
final class Split {
    final private boolean numberFormat;
    final private int sizeInLines;//\
    final private int sizeInChars;//-одно из трех(или не одного)
    final private int countOfFiles;//
    final private String nameOfOutFiles;
    final private String nameOfInFile;

    Split(boolean numberFormat, int sizeInLines, int sizeInChars, int countOfFiles,
                 String nameOfOutFiles, String nameOfInFile) {
        if (sizeInChars != 0 && sizeInLines != 0 || sizeInChars != 0 && countOfFiles != 0 ||
                sizeInLines != 0 && countOfFiles != 0){
            throw new IllegalArgumentException("Не может быть задано несколько вариантов деления файла одновременно");
        }
        this.numberFormat = numberFormat;
        this.sizeInLines = sizeInLines;
        this.sizeInChars = sizeInChars;
        this.countOfFiles = countOfFiles;
        this.nameOfOutFiles = nameOfOutFiles;
        this.nameOfInFile = nameOfInFile;
    }

    void writeInFiles() throws IOException{
        //Название выходных файлов
        String out;
        if (this.nameOfOutFiles == null){
            out = "x";
        }
        else{
            if (this.nameOfOutFiles.equals("-")){
                out = this.nameOfInFile.substring(0,this.nameOfInFile.indexOf("."));
            }
            else{
                out = this.nameOfOutFiles;
            }
        }
        //Чтение и запись
        if (this.sizeInLines == 0 && this.countOfFiles == 0 && this.sizeInChars == 0)
            writeInFilesIfSizeInLines(out, 100);
        if (this.sizeInLines != 0)
            writeInFilesIfSizeInLines(out, this.sizeInLines);
        if (this.countOfFiles != 0)
            writeInFilesIfSizeInChars(out, this.lengthOfFile(nameOfInFile) / this.countOfFiles + 1);
        if (this.sizeInChars != 0)
            writeInFilesIfSizeInChars(out, this.sizeInChars);
    }

    private void writeInFilesIfSizeInLines(String out, int size) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(this.nameOfInFile));
        String str;
        int i = 0;
        int numFile = 0;
        BufferedWriter bw = new BufferedWriter(new FileWriter(out + numFormat(1) + ".txt"));
        while ((str = br.readLine()) != null) {
            if (numFile < i / size + 1) {
                numFile = i / size + 1;
                bw.close();
                bw = new BufferedWriter(new FileWriter(out + numFormat(numFile) + ".txt"));
            }
            else{
                bw.newLine();
            }
            bw.write(str);
            i++;
        }
        bw.close();
    }

    private void writeInFilesIfSizeInChars(String out, int size) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(this.nameOfInFile));
        int ch;
        int i = 0;
        int numFile = 0;
        BufferedWriter bw = new BufferedWriter(new FileWriter(out + numFormat(1) + ".txt"));
        while ((ch = br.read()) != -1) {
            if (numFile < i / size + 1) {
                numFile = i / size + 1;
                bw.close();
                bw = new BufferedWriter(new FileWriter(out + numFormat(numFile) + ".txt"));
            }
            bw.write(ch);
            i++;
        }
        bw.close();
    }

    private int lengthOfFile(String nameOfFile) throws IOException{
        int length = 0;
        BufferedReader br = new BufferedReader (new FileReader(nameOfFile));
        while(br.read()!=-1){
            length++;
        }
        return length;
    }

    private String numFormat(Integer d) {
        if (this.numberFormat) {
            return d.toString();
        }
        else {
            StringBuilder sb = new StringBuilder();
            int dd = d - 1;
            if (d == 1){
                sb.append("a");
            }
            while (dd != 0) {
                sb.append((char) ((dd % 26) + 97));
                dd = dd / 26;
            }
            if (d < 27) {
                sb.append("a");
            }
            return sb.reverse().toString();
        }
    }
}