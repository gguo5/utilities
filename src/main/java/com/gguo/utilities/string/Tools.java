/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gguo.utilities.string;


import java.util.zip.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Vector;
import java.sql.*;

public class Tools {

    static final char[] hexDigit={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    public static final int LEFT= 1;
    public static final int CENTRE= 2;
    public static final int RIGHT= 3;
    public static final int PadLeft= 1;
    public static final int PadCentre= 2;
    public static final int PadRight= 3;
    
    public Tools() {
    }

    public static String removeChar(String Source, char ch) {
  
        StringBuffer sBuff= new StringBuffer("");
  
        for (int idx= 0; idx < Source.length();  idx++) {
        if (Source.charAt(idx) != ch) {                      // This is a valid character
            sBuff.append(Source.charAt(idx));
        }
     }
    return sBuff.toString();
  }
 
    public static String cleanString(String source, int maxLen) {
        String returnStr= source.replaceAll("\'", "").replaceAll("\"", "");
    
        if (returnStr.length()> maxLen) {
            return returnStr.substring(0, maxLen);
        } else {
            return returnStr;
        }
    }
  
    public static void Sleep(int sleepTime) {
          
        Thread sleeping= new Thread();
        try {
            sleeping.sleep(sleepTime* 1000);
        } catch(Exception Ex) {
        }
    }
 

    public static String unZIP(byte[] Source) {
        ByteArrayInputStream bins= new ByteArrayInputStream(Source);
        byte[] buf= new byte[2048];
        StringBuffer rString= new StringBuffer("");
        int len;
         
        try {
            GZIPInputStream zipit= new GZIPInputStream(bins);
            while ((len = zipit.read(buf)) > 0) {
                 rString.append(new String(buf).substring(0, len));
            }
            return rString.toString();
        } catch (Exception Ex) {
           return "";
        }
    }

    public static byte[] unZIPtoBytes(byte[] Source) {
        ByteArrayInputStream bins= new ByteArrayInputStream(Source);
        byte[] buf= new byte[2048];
        ByteArrayOutputStream bos= new ByteArrayOutputStream(Source.length* 8);
        int len;
         
        try {
            GZIPInputStream zipit= new GZIPInputStream(bins);
            while ((len = zipit.read(buf)) > 0) {
                bos.write(buf, 0, len);
           //      rString.append(new String(buf).substring(0, len));
            }
            bos.close();
            return bos.toByteArray();
        } catch (Exception Ex) {
           return null;
        }
    }    
    
    public static String unZIPCsharp(byte[] Source) {
        byte[] Csharp= new byte[Source.length- 4];
        int pos= 0;
        for (int idx= 4; idx< Source.length;  pos++, idx++ ) {
            Csharp[pos]= Source[idx];
        }
        return unZIP(Csharp);
      }

      public static byte[] ZIP(String source) {
          ByteArrayOutputStream bos= new ByteArrayOutputStream(source.length()* 4);

          try {
              GZIPOutputStream outZip= new GZIPOutputStream(bos);
              outZip.write(source.getBytes());
              outZip.flush();
              outZip.close();
          } catch (Exception Ex) {
          }
          return bos.toByteArray();
      }

      public static byte[] ZIP(byte[] source) {
          ByteArrayOutputStream bos= new ByteArrayOutputStream(source.length* 4);

          try {
              GZIPOutputStream outZip= new GZIPOutputStream(bos);
              outZip.write(source);
              outZip.flush();
              outZip.close();
          } catch (Exception Ex) {
          }
          return bos.toByteArray();
      }
      
      public static String toHexString(byte[] data) {
          StringBuffer hexString= new StringBuffer("");
          
          for(int idx= 0;  idx< data.length; idx++) {
              hexString.append(hexDigit[(data[idx] & 0xF0) >> 4]+ ""+hexDigit[data[idx] & 0x0F]);
          }
          return hexString.toString();
      }
      
      public static byte[] fromHexString(String hexString) {

         hexString= hexString.toUpperCase();
         byte[] bytes= new byte[(hexString.length()/2)+ 2];
         long code;
         int idx1= 0;
         char digit;
         
         for (int idx= 0;  idx< hexString.length(); idx+= 2) {
            digit= hexString.charAt(idx);
            code= (digit>= 'A') ? ((10+ (digit- 'A')) << 4) & 0xF0 : ((digit- '0') << 4) & 0xF0;
            digit= hexString.charAt(idx+ 1);
            code+= (digit>= 'A') ? (10+ (digit- 'A')) : (digit- '0');
            bytes[idx1]= (byte) (code & 0xFF);
            idx1++;
         }
          return bytes;
      }

   public static String[] StringSplit(String data, char delim) {
        int dLen= data.length();
        int counter= 0;
//        String[] newData= null;
        for (int idx= 0;  idx< dLen;  idx++) {
            if (data.charAt(idx)== delim) {
                counter++;
            }
        }
        String[] newData= new String[counter+ 1];
        counter= 0;
        int start= 0;
        for (int idx= 0;  idx< dLen;  idx++) {
            if (data.charAt(idx)== delim) {
                newData[counter]= data.substring(start, idx);
                start= idx+ 1;
                counter++;
            }
        }
        newData[counter]= data.substring(start);
        return newData;
    }

    public static String joinAll(String[] source, char sep) {
        StringBuffer retStr= new StringBuffer("");
        for (int idx= 0; idx< source.length;  idx++) {
            retStr.append(source[idx]);
            if (idx< source.length- 1) {
                retStr.append(sep);
            }
        }
        return retStr.toString();
    }
        
    public static String join(String[] string, char separator) {
        StringBuffer result = new StringBuffer();
        if (string.length > 0) {
            if (string[0] != null){
                result.append(string[0]);
            }else{
                result.append("");
            }
            for (int i=1; i<string.length; i++) {
                if (separator != (char)0){
                    result.append(separator);
                }
                if (string[i] != null){
                    result.append(string[i]);
                } else {
                    result.append("");
                }
            }
        }
        return stripExtraSeparators(result.toString(), separator);
    }
    
    private static String stripExtraSeparators(String string, char separator){
        while (string.endsWith(String.valueOf(separator))) {
            string = string.substring(0, string.length() - 1 );
        }
        return string;
    }    

    public static String genUID(String Name, int maxSize) {
        String returnStr= Name+ System.currentTimeMillis();
        try {
            SecureRandom Rnd= new SecureRandom().getInstance("SHA1PRNG");
            SecureRandom Rnd1= new SecureRandom().getInstance("SHA1PRNG");
            Rnd.setSeed(Rnd1.nextLong());
            Rnd1.setSeed(Rnd.nextLong());
            Rnd.setSeed(Rnd1.nextLong());
            returnStr+= Rnd.nextLong();
        } catch (Exception Ex) {
            Random theRnd= new Random();
            theRnd.setSeed(System.currentTimeMillis());
            returnStr+= theRnd.nextLong();
        }
        if (returnStr.length()> maxSize) {
            returnStr= returnStr.substring(returnStr.length()- maxSize);
        }
        return returnStr;
    }
    
    public static String leadingZeros(String data, int size) {
        return justify(data, '0', size, PadRight);
    }
 
    public static String justify(String data, char padChar, int size, int mode) {
        StringBuffer newStr= new StringBuffer(data);
        try {
            switch (mode) {
                case 1: 
                        while (newStr.length() < size) {
                            newStr.append(padChar);
                        }
                        break;
                case 2: 
                        boolean front= true;
                        while (newStr.length()< size) {
                            if (front) {
                                newStr.insert(0, padChar);
                            } else {
                                newStr.append(padChar);
                            }
                            front= !front;
                        }
                        break;
                case 3: 
                        while (newStr.length()< size) {
                            newStr.insert(0, padChar);
                        }
                        break;
                default:
                        
            }
        } catch (Exception Ex) {
            newStr= new StringBuffer(data);
        }
        return newStr.toString();
    }

    public static String stripLeadingChars( String Number )
    {
        int idx = 0;
        for (; idx < Number.length() && !Character.isDigit( Number.charAt( idx ) ); idx++);

        Number= idx < Number.length()? Number.substring( idx ): "";
        return Number;
    }

    public static String stripTrailingChars( String Number )
    {
        int idx = Number.length()- 1;
        for (; idx >= 0 && !Character.isDigit( Number.charAt( idx ) ); idx--);

        Number= idx < Number.length()? Number.substring(0, idx ): "";
        return Number;
    }

    public static String WordWrap(String Data, int LineLength, String LB, String LineBreak) {
        StringBuffer newData= new StringBuffer("");
        if (LineBreak.length()== 0) {
            LineBreak= LB;
        }
        try {
            String split= LB.replaceAll("\\\\", "\\\\\\\\");
            String[] lines= Data.split(split);
            for (int idx= 0;  idx< lines.length;  idx++) {
                StringBuffer newLine= new StringBuffer("");
                String[] words= StringSplit(lines[idx], ' ');
                for (int idx1= 0;  idx1< words.length;  idx1++) {
                    if (newLine.length()+ words[idx1].length()> LineLength) {
                        newData.append(newLine).append(LineBreak);
                        newLine.setLength(0);
                    }
                    if (newLine.length()> 0) {
                        newLine.append(' ');
                    }
                    newLine.append(words[idx1]);
                }
                newData.append(newLine).append(LineBreak);
            }
        } catch (Exception Ex) {
            Ex.printStackTrace();
            return Data;
        }
        return newData.toString();
    }
    
    public static String FormatElapseTime(String Format, long offset) {
        return FormatGivenElapseTime(Format, System.currentTimeMillis()- offset);
    }
            
    public static String FormatGivenElapseTime(String Format, long deltaTime) {
        try {
            int days = (int) deltaTime / 86400000;
            deltaTime -= days * 86400000;
            int hours = (int) deltaTime / 3600000;
            deltaTime -= hours * 3600000;
            int minute = (int) deltaTime / 60000;
            deltaTime -= minute * 60000;
            int second = (int) deltaTime / 1000;
            deltaTime -= second * 1000;
            // faulty = days > 0 || hours > 0 || minute > 30 ? "*" : " ";
            StringBuffer data= new StringBuffer(Format);
            int daySize= findSize(data, "%D");
            int hourSize= findSize(data, "%H");
            int minuteSize= findSize(data, "%M");
            int secondSize= findSize(data, "%S");
            int milliSize= findSize(data, "%s");
            Format= data.toString();
            Format= Format.replaceFirst("%D", Tools.justify("" + days, '0', daySize, Tools.PadRight ) );
            Format= Format.replaceFirst("%H", Tools.justify( "" + hours, '0', hourSize, Tools.PadRight ) );
            Format= Format.replaceFirst("%M", Tools.justify( "" + minute, '0', minuteSize, Tools.PadRight ) );
            Format= Format.replaceFirst("%S", Tools.justify( "" + second, '0', secondSize, Tools.PadRight ) );
            Format= Format.replaceFirst("%s", Tools.justify( "" + deltaTime, '0', milliSize, Tools.PadLeft ) );
        } catch (Exception Ex) {
        }
        return Format;
    }
    
    private static int findSize(StringBuffer data, String Search) {
        int idx= data.indexOf(Search);
        int size= 0;
        if (idx== -1) {
            return size;
        }
        idx+= Search.length();
        try {
            int end= idx;
            int c= idx;
            for (;  c< data.length(); c++ ) {
                if (!Character.isDigit(data.charAt(c))) {
                    end= c;
                    c= -1;
                    break;
                }
            }
            if (c> data.length()) {
                return size;
            }
            if (c!= -1) {
                end= c;
            }
            size= Integer.parseInt(data.substring(idx, end));
            data.replace(size, end, "");
        } catch (Exception Ex) {
            size= 0;
        }
        return size;
    }
    
    public static String truncate(String data, int Size) {
        if (data.length()> Size) {
            return data.substring(0, Size);
        } else {
            return data;
        }
    }
    
    public static Vector WordSplit(String data) {
        Vector Words= new Vector();
        int startWord= 0;
        boolean foundWord= false;
        char letter= ' ';
        for (int idx= 0;  idx< data.length();  idx++) {
            letter= data.charAt(idx);
            if ((letter== 32 || letter== 9) && foundWord) {
                Words.add(data.substring(startWord, idx));
                foundWord= false;
            } else if (letter> 32 && !foundWord) {
                foundWord= true;
                startWord= idx;
            }
        }
        if (foundWord) {
            Words.add(data.substring(startWord));
        }
        return Words;
    }
    
    private static String genUID() {
        long seed= 0;
        try {
            SecureRandom Rnd= new SecureRandom().getInstance("SHA1PRNG");
            SecureRandom Rnd1= new SecureRandom().getInstance("SHA1PRNG");
            Rnd.setSeed(Rnd1.nextLong());
            Rnd1.setSeed(Rnd.nextLong());
            Rnd.setSeed(Rnd1.nextLong());
            seed= Rnd.nextLong();
        } catch (Exception Ex) {
            Random theRnd= new Random();
            theRnd.setSeed(System.currentTimeMillis());
            seed= theRnd.nextLong();
        }
        return Tools.justify(Long.toHexString(seed), '0', 16, Tools.RIGHT).toUpperCase();
    }

    public static String genUUID() {

        StringBuilder theGUI= new StringBuilder(genUID()).append('-').append(genUID());
        theGUI.insert(12, '-');
        theGUI.insert(8, '-');
        theGUI.insert(23, '-');
        return theGUI.toString();
    }

    public static void closeDB(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs!= null) rs.close();
        } catch (Exception Ex) {
        }
        try {
            if (stmt!= null) stmt.close();
        } catch (Exception Ex) {
        }
        try {
            if (conn!= null) conn.close();
        } catch (Exception Ex) {
        }
    }

    public static void closeDB(ResultSet rs, CallableStatement cstmt, Connection conn) {
        try {
            if (rs!= null) rs.close();
        } catch (Exception Ex) {
        }
        try {
            if (cstmt!= null) cstmt.close();
        } catch (Exception Ex) {
        }
        try {
            if (conn!= null) conn.close();
        } catch (Exception Ex) {
        }
    }
    
    
    public static String ExceptionToString(Exception Ex, int width) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        Ex.printStackTrace(pw);
        if (width== 0) {
            return sw.toString();
        }
        return WordWrap(sw.toString(), width, "\n") ;
    }

    public static String WordWrap(String Data, int LineLength, String LineBreak) {
        StringBuffer newData= new StringBuffer("");
        if (LineBreak.length()== 0) {
            LineBreak= "\n";
        }
        try {
            StringBuffer newLine= new StringBuffer("");
            String[] words= Tools.StringSplit(Data, ' ');
            for (int idx1= 0;  idx1< words.length;  idx1++) {
                if (newLine.length()+ words[idx1].length()> LineLength) {
                    newData.append(newLine).append(LineBreak);
                    newLine.setLength(0);
                }
                if (newLine.length()> 0) {
                    newLine.append(' ');
                }
                newLine.append(words[idx1]);
            }
            newData.append(newLine).append(LineBreak);
        } catch (Exception Ex) {
            Ex.printStackTrace();
            return Data;
        }
        return newData.toString();
    }

    
/*    public static int findHAPIrepeat(ca.uhn.hl7v2.util.Terser T, String Addr, String match) {
        try {
            for (int I= 0;  ; I++) {
                if (T.get(Addr.replaceFirst("\\?", ""+ I)).equalsIgnoreCase(match)) {
                    return I;
                }
            }
        } catch (Exception E) {
            return -1;
        }
    }
    */
}
