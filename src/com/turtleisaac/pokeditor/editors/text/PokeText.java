//package text;
//
//import java.io.*;
//import java.util.*;
//
//import framework.*;
//
//
//public class PokeText {
//    private static int originalKey;
//    private static String path = System.getProperty("user.dir") + File.separator; //creates a new String field containing user.dir and File.separator (/ on Unix systems, \ on Windows)
//    private String dataPath = path;
//
//    public static void main(String[] args) throws IOException
//    {
//        PokeText text= new PokeText();
//        text.readText("msg2");
//    }
//
//
////    private static int getRealSize(String p)
////    {
////        int size=0;
////        for(int i=0;i<p.length();i++)
////        {
////            if(p.charAt(i)=='\\')
////            {
////                if (p.charAt(i+1) == 'r')
////                {
////                    size++;
////                    i++;
////                }
////                else if (p.charAt(i+1) == 'n')
////                {
////                    size++;
////                    i++;
////                }
////                else if (p.charAt(i+1) == 'f')
////                {
////                    size++;
////                    i++;
////                }
////                else if (p.charAt(i+1) == 'v')
////                {
////                    size+=2;
////                    i += 5;
////                }
////                else if ((p.charAt(i+1) == 'x') && (p.charAt(i + 2) == '0') && (p.charAt(i + 3) == '0') && (p.charAt(i + 4) == '0') && (p.charAt(i + 5) == '0'))
////                {
////                    size++;
////                    i += 5;
////                }
////                else if ((p.charAt(i+1) == 'x') && (p.charAt(i + 2) == '0') && (p.charAt(i + 3) == '0') && (p.charAt(i + 4) == '0') && (p.charAt(i + 5) == '1'))
////                {
////                    size++;
////                    i += 5;
////                }
////                else
////                {
////                    size++;
////                    i += 5;
////                }
////            }
////            else if(p.charAt(i)=='[')
////            {
////                if (p.charAt(i + 1) == 'P')
////                {
////                    size++;
////                    i += 3;
////                }
////                if (p.charAt(i + 1) == 'M')
////                {
////                    size++;
////                    i += 3;
////                }
////            }
////            else
////            {
////                size++;
////            }
////        }
////        size++;
////        return size;
////    }
//
////    private static int[] encodeText(String str, int stringSize)
////    {
////        int[] numArray=new int[stringSize-1];
////        int index=0;
////        for(int i=0;i<str.length();i++)
////        {
////            if(str.charAt(i)=='\\')
////            {
////                if(str.charAt(i+1)=='r')
////                {
////                    numArray[index]=0x25bc;
////                    i++;
////                }
////                else if(str.charAt(i+1)=='n')
////                {
////                    numArray[index]=0xe000;
////                    i++;
////                }
////                else if(str.charAt(i+1)=='n')
////                {
////                    numArray[index]=0xe000;
////                    i++;
////                }
////                else if(str.charAt(i+1)=='f')
////                {
////                    numArray[index]=0x25bd;
////                    i++;
////                }
////                else if(str.charAt(i+1)=='v')
////                {
////                    numArray[index]=0xfffe;
////                    index++;
////                    numArray[index]=Integer.parseInt(str.substring(i+2, i+6), 16);
////                    i+=5;
////                }
////                else if((((str.charAt(i+1) == 'x') && (str.charAt(i + 2) == '0')) && ((str.charAt(i + 3) == '0') && (str.charAt(i + 4) == '0'))) && (str.charAt(i + 5) == '0'))
////                {
////                    numArray[index] = 0;
////                    i += 5;
////                }
////                else if((((str.charAt(i+1) == 'x') && (str.charAt(i + 2) == '0')) && ((str.charAt(i + 3) == '0') && (str.charAt(i + 4) == '0'))) && (str.charAt(i + 5) == '1'))
////                {
////                    numArray[index] = 1;
////                    i += 5;
////                }
////                else
////                {
////                    numArray[index]=Integer.parseInt(str.substring(i+2, i+6), 16);
////                    i+=5;
////                }
////            }
////            else
////            {
////                numArray[index]=charTable.writeCharacter(String.valueOf(str.charAt(i)));
////            }
////            index++;
////        }
////        return numArray;
////    }
//
//    public void readText(String textDir) throws IOException
//    {
//        dataPath += textDir;
//
//        Buffer buffer;
//        CharTable charTable= new CharTable();
//        BufferedWriter writer= new BufferedWriter(new FileWriter(path + File.separator + "ExtractedText3.txt"));
//        ArrayList<String> dataList= new ArrayList<>();
//
//        List<File> fileList= new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dataPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
//        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden
//
//        File[] files= fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
//        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)
//        File file;
//
//        int stringCount;
//        boolean flag=false, flag2=false;
//        for(int i= 0; i < files.length; i++)
//        {
//            file= files[i];
//
//            writer.write("\nBank: " + i + "\n");
//            buffer= new Buffer(file.toString());
//
//            try
//            {
//                stringCount= buffer.readUIntS();
//                System.out.println("File: " + i + ", " + stringCount);
//                originalKey= buffer.readUIntS();
//                int num=((originalKey*0x2fd)&0xffff);
//                int num2=0, num3=0, num4=0;
//                int numArray[]=new int[stringCount];
//                int numArray2[]=new int[stringCount];
//
//                for(int x=0;x<stringCount;x++)
//                {
//                    num2=(num*(x+1))&0xffff;
//                    num3=num2 | (num2<<16);
//                    numArray[x]= buffer.readInt();
//                    numArray[x]=numArray[x] ^ num3;
//                    numArray2[x]= buffer.readInt();
//                    numArray2[x]=numArray2[x] ^ num3;
//                }
//                for(int j=0;j<stringCount;j++)
//                {
//                    num=((0x91bd3 * (j+1)) & 0xffff);
//                    String text = "";
//                    for(int k=0;k<numArray2[j];k++)
//                    {
//                        num4= buffer.readUIntS();
//                        num4=(num4^num);
//                        if (num4 == 57344 || num4 == 9660 || num4 == 9661 || num4 == 61696 || num4 == 65534 || num4 == 65535)
//                        {
//                            if (num4 == 57344)
//                                text += ("\\n");
//                            if (num4 == 9660)
//                                text += ("\\r");
//                            if (num4 == 9661)
//                                text += ("\\f");
//                            if (num4 == 61696)
//                                flag2 = true;
//                            if (num4 == 65534)
//                            {
//                                text += ("\\v");
//                                flag = true;
//                            }
//                        }
//                        else
//                        {
//                            if (flag)
//                            {
//                                if(Integer.toHexString(num4).length()<4)
//                                {
//                                    String temp="";
//                                    for(int index=Integer.toHexString(num4).length();index<4;index++)
//                                        temp+=("0");
//                                    text+=(temp+Integer.toHexString(num4));
//                                }
//                                else
//                                    text += Integer.toHexString(num4);
//                                flag = false;
//                            }
//                            else
//                            {
//                                if (flag2)
//                                {
//                                    int num5 = 0;
//                                    int num6 = 0;
//                                    String str = null;
//                                    while (true)
//                                    {
//                                        if (num5 >= 15)
//                                        {
//                                            num5 -= 15;
//                                            if (num5 > 0)
//                                            {
//                                                int num8 = (num6 | (num4 << 9 - num5 & 511));
//                                                if ((num8 & 255) == 255)
//                                                    break;
//                                                if (num8 != 0 && num8 != 1)
//                                                {
//                                                    String str2 = charTable.getCharacter(num8);
//                                                    text += str2;
//                                                    if (str2==("0"))
//                                                    {
//                                                        if(Integer.toHexString(num8).length()<4)
//                                                        {
//                                                            String temp="";
//                                                            for(int index=Integer.toHexString(num8).length();index<4;index++)
//                                                                temp+=("0");
//                                                            text+="\\x"+temp+Integer.toHexString(num8);
//                                                        }
//                                                        else
//                                                            text += "\\x"+Integer.toHexString(num8);
//                                                    }
//                                                }
//                                            }
//                                        }
//                                        else
//                                        {
//                                            int num8 = (num4 >> num5 & 511);
//                                            if ((num8 & 255) == 255)
//                                                break;
//                                            if (num8 != 0 && num8 != 1)
//                                            {
//                                                String str3 = charTable.getCharacter(num8);
//                                                text += str3;
//                                                if (str3==("0"))
//                                                {
//                                                    if(Integer.toHexString(num8).length()<4)
//                                                    {
//                                                        String temp="";
//                                                        for(int index=Integer.toHexString(num8).length();index<4;index++)
//                                                            temp+=("0");
//                                                        text+="\\x"+temp+Integer.toHexString(num8);
//                                                    }
//                                                    else
//                                                        text += "\\x"+Integer.toHexString(num8);
//                                                }
//                                            }
//                                            num5 += 9;
//                                            if (num5 < 15)
//                                            {
//                                                num6 = (num4 >> num5 & 511);
//                                                num5 += 9;
//                                            }
//                                            num += 18749;
//                                            num &= 65535;
//                                            num4= buffer.readUIntS();
//                                            num4^=num;
//                                            k++;
//                                        }
//                                    }
//                                    text += str;
//                                }
//                                else
//                                {
//                                    String str3 = charTable.getCharacter(num4);
//                                    text += str3;
//                                    if (str3 == ("0"))
//                                    {
//                                        if(Integer.toHexString(num4).length()<4)
//                                        {
//                                            String temp="";
//                                            for(int index=Integer.toHexString(num4).length();index<4;index++)
//                                                temp+=("0");
//                                            text+="\\x"+temp+Integer.toHexString(num4);
//                                        }
//                                        else
//                                            text += "\\x"+Integer.toHexString(num4);
//                                    }
//                                }
//                            }
//                        }
//                        num += 18749;
//                        num &= 65535;
//                    }
//                    System.out.print("    ");
//
//                    String str= text.toString();
//                    StringBuilder line= new StringBuilder();
//
//                    if(str.contains("\\v"))
//                    {
////                        if(i == 17)
////                        {
////                            System.out.println("MOOO");
////                        }
//                        while(str.contains("\\v"))
//                        {
//                            int idx= str.indexOf("\\v");
//                            if(idx != 0)
//                            {
//                                line.append(str, 0, idx);
//                            }
//                            str= str.substring(idx + 2);
//                            line.append("VAR(");
//
//                            line.append(Integer.parseInt(str.substring(0, 4), 16)).append(",");
//                            str= str.substring(str.indexOf("\\") + 3);
//
//
//                            if(str.indexOf("\\\\x") == 4)
//                            {
//                                str= str.substring(str.indexOf("\\") + 3);
//                                line.append(Integer.parseInt(str.substring(0,4),16));
//                                str= str.substring(4);
//                            }
//                            else
//                            {
//                                line.append(charTable.writeCharacter(str.substring(4,5)));
//                                str= str.substring(5);
//                            }
//                            line.append(")");
//
////                            str= str.substring(3);
//                        }
//                        line.append(str);
//                    }
//                    else
//                    {
//                        line.append(str);
//                    }
//
//                    dataList.add(line.toString());
//                    System.out.println(line.toString());
//                    writer.write("    " + line.toString() + "\n");
//                    writer.flush();
//                }
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//                writer.write("ERROR\n");
//                writer.flush();
//            }
//        }
//
//    }
//
////    public static void saveText(FimgEntry fs, ArrayList<String> textList)
////    {
////        ByteArrayOutputStream out=new ByteArrayOutputStream();
////        try {
////            EndianUtils.writeSwappedShort(out, (short) textList.size());
////            EndianUtils.writeSwappedShort(out, (short) originalKey);
////            int num=(originalKey*0x2fd)&0xffff, num2=0, num3=0, num4=4+(textList.size()*8);
////            int[] numArray=new int[textList.size()];
////            for(int i=0;i<textList.size();i++)
////            {
////                num2=(num*(i+1))&0xffff;
////                num3=num2|(num2<<16);
////                EndianUtils.writeSwappedInteger(out,(num4^num3));
////                numArray[i]=getRealSize(textList.get(i));
////                EndianUtils.writeSwappedInteger(out, (getRealSize(textList.get(i))^num3));
////                num4+=getRealSize(textList.get(i))*2;
////            }
////            for(int j=0;j<textList.size();j++)
////            {
////                num=(0x91bd3*(j+1))&0xffff;
////                int[] numArray2=encodeText(textList.get(j),numArray[j]);
////                for(int k=0;k<numArray[j]-1;k++)
////                {
////                    EndianUtils.writeSwappedShort(out, (short)(numArray2[k]^num));
////                    num+=0x493d;
////                    num&=0xffff;
////                }
////                EndianUtils.writeSwappedShort(out, (short)(0xffff^num));
////            }
////            fs.setEntryData(out.toByteArray());
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }
////
////    public static String simplifyText(String str)
////    {
////        String txt="";
////        for(int i=0;i<str.length();i++)
////        {
////            if(str.charAt(i)=='\\')
////            {
////                switch(str.charAt(i+1))
////                {
////                    case 'n':
////                        txt+=String.valueOf('\n');
////                        i++;
////                        break;
////                    case 'r':
////                        txt+=String.valueOf('\n')+String.valueOf('\n');
////                        i++;
////                        break;
////                    default:
////                        break;
////                }
////            }
////            else
////            {
////                txt+=String.valueOf(str.charAt(i));
////            }
////        }
////        return txt;
////    }
////
////    public static String translateText(String str)
////    {
////        String txt="";
////        for(int i=0;i<str.length();i++)
////        {
////            if(str.charAt(i)=='\r' && str.charAt(i+1)=='\n' && str.charAt(i+2)!='\r' && str.charAt(i+3)!='\n')
////            {
////                txt+="\\n";
////                i++;
////            }
////            else if(str.charAt(i)=='\r' && str.charAt(i+1)=='\n' && str.charAt(i+2)=='\r' && str.charAt(i+3)=='\n')
////            {
////                txt+="\\r";
////                i+=3;
////            }
////            else
////                txt+=String.valueOf(str.charAt(i));
////        }
////        return txt;
////    }
//
//    private void sort(File[] arr) {
//        Arrays.sort(arr, Comparator.comparingInt(PokeText::fileToInt));
//    }
//
//    private static int fileToInt(File f) {
//        return Integer.parseInt(f.getName().split("\\.")[0]);
//    }
//
//    private int arrIdx;
//    private String[] input;
//
//    private void initializeIndex(String[] arr) {
//        arrIdx = 0;
//        input = arr;
//    }
//
//    private String next() {
//        try {
//            return input[arrIdx++];
//        } catch (IndexOutOfBoundsException e) {
//            return "";
//        }
//    }
//}