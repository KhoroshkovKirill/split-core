import java.io.*;

/**
 * Created by khoroshkovkirill on 01.04.17.
 */
final class Split {
    final private boolean numberFormat;// -d
    final private int sizeOfOutFile;
    final private boolean isSizeInLines;//true -> sizeInLines, false -> sizeInChars
    final private int countOfDigits;
    final private String nameOfOutFiles;
    final private String nameOfInFile;
    final private int countOfFiles;

    Split(boolean numberFormat, int sizeInLines, int sizeInChars, int countOfFiles,
                 String nameOfOutFiles, String nameOfInFile) throws IOException {
        if (sizeInChars != 0 && sizeInLines != 0 || sizeInChars != 0 && countOfFiles != 0 ||
                sizeInLines != 0 && countOfFiles != 0){
            throw new IllegalArgumentException("Не может быть задано несколько вариантов деления файла одновременно");
        }
        this.numberFormat = numberFormat;
        this.nameOfInFile = nameOfInFile;
        //Имена выходных файлов:
        if (nameOfOutFiles == null) {
            this.nameOfOutFiles = "x";
        } else {
            if (nameOfOutFiles.equals("-")) {
                this.nameOfOutFiles = this.nameOfInFile.substring(0, this.nameOfInFile.indexOf("."));
            } else {
                this.nameOfOutFiles = nameOfOutFiles;
            }
        }
        //Размер выходных файлов:
        if (countOfFiles != 0) {
            this.isSizeInLines = false;
            int length = lengthOfFile();
            this.sizeOfOutFile = (length % countOfFiles) > 0 ?  (length / countOfFiles + 1) : (length / countOfFiles);
            this.countOfFiles = length < countOfFiles ? length : countOfFiles;
        } else {
            if (sizeInChars != 0) {
                this.isSizeInLines = false;
                this.sizeOfOutFile = sizeInChars;
            } else if (sizeInLines != 0) {
                this.isSizeInLines = true;
                this.sizeOfOutFile = sizeInLines;
            } else {
                this.isSizeInLines = true;
                this.sizeOfOutFile = 100;
            }
            int length = lengthOfFile();
            this.countOfFiles = length % sizeOfOutFile > 0 ?  length / sizeOfOutFile + 1 : length / sizeOfOutFile;
        }
        //Количество разрядов в нумерации выходных файлов:
        this.countOfDigits = digitNumber(this.countOfFiles);
    }

    String[] writeInFiles() throws IOException{//возвращает массив имен для тестов
        String[] namesOfOutFiles = new String[countOfFiles];
        BufferedReader br = new BufferedReader(new FileReader(this.nameOfInFile));
        if (this.isSizeInLines){
            String currLine;
            for (int num = 1; num <= this.countOfFiles; num++) {
                String currName = buildName(num);
                namesOfOutFiles[num-1] = currName;
                BufferedWriter bw = new BufferedWriter(new FileWriter(currName));
                bw.write(br.readLine());
                for (int i = 1; i < this.sizeOfOutFile; i++) {
                    if ((currLine = br.readLine()) != null) {
                        bw.newLine();
                        bw.write(currLine);
                    } else {
                        break;
                    }
                }
                bw.close();
            }
        }
        else {
            int currChar;
            for (int num = 1; num <= this.countOfFiles; num++) {
                String currName = buildName(num);
                namesOfOutFiles[num-1] = currName;
                BufferedWriter bw = new BufferedWriter(new FileWriter(currName));
                for (int i = 0; i < this.sizeOfOutFile; i++) {
                    if ((currChar = br.read()) != -1) {
                        bw.write(currChar);
                    } else {
                        break;
                    }
                }
                bw.close();
            }
        }
        return namesOfOutFiles;
    }

    private int lengthOfFile() throws IOException{
        int length = 0;
        BufferedReader br = new BufferedReader (new FileReader(this.nameOfInFile));
        if (this.isSizeInLines) {
            while (br.readLine() != null) {
                length++;
            }
        }
        else {
            while (br.read() != -1) {
                length++;
            }
        }
        return length;
    }

    private int digitNumber(int num){
        int count = 0;
        if (this.numberFormat) {
            do {
                num /= 10;
                count++;
            } while (num > 0);
        }
        else {
            num--;//т.к. нумерация с нуля('a')
            do {
                num /= 26;// a-z
                count++;
            } while (num > 0);
        }
        return count;
    }

    private String buildName(Integer num) {// формат x/0x/00x...
        if (this.numberFormat) {
            return this.nameOfOutFiles + String.format("%0" + this.countOfDigits + "d", num) +".txt";
        }
        else {
            StringBuilder sb = new StringBuilder();
            num--;
            if (num == 0){
                sb.append("a");
            }
            while (num != 0) {
                sb.append((char) ((num % 26) + 'a'));
                num /= 26;
            }
            for (int i = sb.length(); i < this.countOfDigits; i++) {
                sb.append("a");
            }
            return this.nameOfOutFiles + sb.reverse().toString() + ".txt";
        }
    }
}