import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;  
import java.io.InputStreamReader; 
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

class SolarTerms1900{
    public static int[] solarTerm1900Dates=new int[24];
    
    public static int[] parseSolarTerms1900(String fileName) throws IOException{       
        Pattern pattern1900SolarTerm = Pattern.compile("\\D+(\\d{4})\\D+(\\d{2})\\D+(\\d{2})");

        //Pattern pattern1900SolarTerm = Pattern.compile("(\\D+)(\\d+)");
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        
        int solarTermOrder=0;
        String solarTerm1900Date;

        int normalYear;
        int normalMonth;
        int normalDay;

        while ((solarTerm1900Date = reader.readLine()) != null) {
            System.out.println(solarTerm1900Date );
            Matcher matcher = pattern1900SolarTerm.matcher(solarTerm1900Date);
            if(matcher.find()){
                normalYear=Integer.parseInt(matcher.group(1));
                normalMonth=Integer.parseInt(matcher.group(2));
                normalDay=Integer.parseInt(matcher.group(3));

                System.out.println("{} "+normalYear+"  "+normalMonth+"  "+normalDay);

                solarTerm1900Dates[solarTermOrder]=normalDay;
                solarTermOrder++;
            }
            else{
                System.out.println("{not found}" );
            }
            
        }
        reader.close();

        PrintWriter pw = new PrintWriter(new FileWriter("solarTerm1900.txt")) ;        

        for(int j=0; j<24; j++){
            pw.printf("%6d",solarTerm1900Dates[j]);
        }
        pw.println();
         
        pw.close();
    
        return solarTerm1900Dates;
    }
}

class SolarTerm1901_2100{

    public static int[][] solarTermDates1901_2100=new int[200][24];

    public static void processSolarTerms1901_2100( ) throws IOException{
        Pattern pattern = Pattern.compile("(\\d{4})\\D{2}(\\d{1,2})\\D{2}(\\d{1,2})");
        int solarTermOrder;

        for(int i=1901;i<=2100;i++){
            solarTermOrder=0;
            String  fileName=""+i+"c.txt";
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            String dateInfo;

            reader.readLine();
            reader.readLine();
            reader.readLine();
         
            while ((dateInfo = reader.readLine()) != null) {
                String[] dateInfoSegments= dateInfo.split("\\s+");

                if(dateInfoSegments.length==4){
                    Matcher matcher = pattern.matcher(dateInfoSegments[0]);
                    System.out.println(dateInfoSegments[0]);
                    if(matcher.find()){
                        int normalYear=Integer.parseInt(matcher.group(1));
                        int normalMonth=Integer.parseInt(matcher.group(2));
                        int normalDay=Integer.parseInt(matcher.group(3));

                        System.out.println("{} "+normalYear+"  "+normalMonth+"  "+normalDay);
                        solarTermDates1901_2100[normalYear-1901][solarTermOrder]=normalDay;
                        solarTermOrder++;   
                    }else
                        System.out.println("{not found} " );             
                }                             
            }
            reader.close();
        }

        PrintWriter pw = new PrintWriter(new FileWriter("solarTerm1901_2100.txt")) ;        
        for(int i=0; i<200; i++){
            for(int j=0; j<24; j++){
                pw.printf("%6d",solarTermDates1901_2100[i][j]);
            }
            pw.println();
        }

        pw.println();         
        pw.close();
    }
}

class SolarTerms1900_2100{
    public static int[] solarTerm1900Dates=SolarTerms1900.solarTerm1900Dates;
    public static int[][] solarTerm1901_2100Dates=SolarTerm1901_2100.solarTermDates1901_2100;

    public static int[][] normalDateOfsolarTermsMinMaxDiff=new int[24][3];
    public static int totalYears=200;
    public static int totalUniqueYears=100;
    public static int[][] NormalDateOfsolarTerms1900_2100Unique=new int[totalUniqueYears][24];

    public static void createNormalDateOfsolarTermsMinMaxDiff() throws IOException{
        int[] solarTerm1900Dates=SolarTerms1900.solarTerm1900Dates;
        int[][] solarTerm1901_2100Dates=SolarTerm1901_2100.solarTermDates1901_2100;
        int i, j, i1, j1;

        for(i=0; i<24; i++){
            normalDateOfsolarTermsMinMaxDiff[i][0]=solarTerm1900Dates[i];
            normalDateOfsolarTermsMinMaxDiff[i][1]=solarTerm1900Dates[i];
        }
        
        for(i=0; i<24; i++){
            for(j=0; j<totalYears; j++){
                if(normalDateOfsolarTermsMinMaxDiff[i][0]>solarTerm1901_2100Dates[j][i]){
                    normalDateOfsolarTermsMinMaxDiff[i][0]=solarTerm1901_2100Dates[j][i];
                }else 
                    if(normalDateOfsolarTermsMinMaxDiff[i][1]<solarTerm1901_2100Dates[j][i])  
                    { 
                        normalDateOfsolarTermsMinMaxDiff[i][1]=solarTerm1901_2100Dates[j][i];
                    }          
            }
        }

        PrintWriter pw = new PrintWriter(new FileWriter("solarTermsMinMaxDiff.txt")) ;        

        for( j=0; j<24; j++){
            pw.printf("%6d   %6d   %6d",normalDateOfsolarTermsMinMaxDiff[j][0],
            normalDateOfsolarTermsMinMaxDiff[j][1],normalDateOfsolarTermsMinMaxDiff[j][2]);
        }
        pw.println();         
        pw.close();

        System.out.println("normalDateOfsolarTermsMinMaxDiff");
        for(i=0; i<24; i++){
            normalDateOfsolarTermsMinMaxDiff[i][2]=normalDateOfsolarTermsMinMaxDiff[i][1]-
            normalDateOfsolarTermsMinMaxDiff[i][0];
            System.out.printf("%16d  %16d  %16d\n",normalDateOfsolarTermsMinMaxDiff[i][0],
            normalDateOfsolarTermsMinMaxDiff[i][1],normalDateOfsolarTermsMinMaxDiff[i][2]);    
        }

        System.out.println();
    }

    public static void createNormalDateOfsolarTerms1900_2100Unique() throws IOException{
        int[] solarTerm1900Dates=SolarTerms1900.solarTerm1900Dates;
        int[][] solarTerm1901_2100Dates=SolarTerm1901_2100.solarTermDates1901_2100;
        int i, j, i1, j1;

        int[][] indices69=new int[69][];
        String[] strings69=new String[69];

        for(i=0; i<69;i++){
            strings69[i]="";
        }
        ArrayList<ArrayList<Integer>> indicesArrayList = new ArrayList<ArrayList<Integer>>();

 
        int uniqueOrder=0;
        for(i=0; i<24; i++){
            NormalDateOfsolarTerms1900_2100Unique[0][i]=solarTerm1900Dates[i];
        }
        ArrayList<Integer> horiArrayList0=new ArrayList<Integer>();
        horiArrayList0.add(-1);
        indicesArrayList.add(horiArrayList0);
        strings69[0]="-1 ";
        uniqueOrder++;

DateVerticalIndex: for(i=0; i<totalYears; i++){         
    System.out.printf("%12c",' ');
    for(int m=0; m<24; m++){System.out.printf("%4d", solarTerm1901_2100Dates[i][m]);}
    System.out.println();
    UniqueIndex:    for(i1=0; i1<uniqueOrder; i1++){
                    System.out.printf("%5d %5d ",i,i1);

                    for(int m=0; m<24; m++){System.out.printf("%4d", NormalDateOfsolarTerms1900_2100Unique[i1][m]);}
                    System.out.println();

                    for(j1=0; j1<24; j1++){
                        if(solarTerm1901_2100Dates[i][j1]!=NormalDateOfsolarTerms1900_2100Unique[i1][j1])   
                            continue UniqueIndex;
                    }

                    if(j1==24){
                        ArrayList<Integer> horiArrayListOrder=indicesArrayList.get(i1);
                        horiArrayListOrder.add(i);

                        strings69[i1]+=" "+i ;
                        //indicesArrayList.add(horiArrayListOrder);
                        continue DateVerticalIndex;
                    }
                                                                
                }
                for(j1=0; j1<24; j1++){
                        NormalDateOfsolarTerms1900_2100Unique[uniqueOrder][j1]=
                        solarTerm1901_2100Dates[i][j1];
                }
                ArrayList<Integer> horiArrayListOrder=new ArrayList<Integer>();
                horiArrayListOrder.add(i);
                indicesArrayList.add(horiArrayListOrder);

                strings69[uniqueOrder]+=" "+i ;
                uniqueOrder++;               
            }
        
            System.out.println(" --------- strings69[i] ---------------------" );
        for(i=0; i<69; i++){
            System.out.printf("%5d %s\n",i,strings69[i]);
        }    
        String diff="";
        StringBuffer sb  = new StringBuffer();
 
        for(  i=0; i<uniqueOrder; i++){

            ArrayList<Integer> horiArrayListOrder=indicesArrayList.get(i);
            int index=horiArrayListOrder.get(0);

            if(index==-1)
            for(int m=0; m<24; m++){
                diff+=solarTerm1900Dates[m]-normalDateOfsolarTermsMinMaxDiff[m][0];
            }
            else{
                for(int m=0; m<24; m++){
                    diff+=solarTerm1901_2100Dates[index][m]-normalDateOfsolarTermsMinMaxDiff[m][0];
                }
            }


        }

        System.out.println(" ----------------------------------");
        System.out.println(diff);
        System.out.println(" ----------------------------------");

        System.out.println("\n"+uniqueOrder+"-------unique_______\n");    
        for(i=0; i<=uniqueOrder; i++){
            System.out.printf("%5d",i);
        for(j=0; j<24; j++){
            System.out.printf("%5d",NormalDateOfsolarTerms1900_2100Unique[i][j]);
        }
        System.out.println();
        }

        char[] solarTermsArray=new char[201];

        for(i=0; i<indicesArrayList.size(); i++){


            ArrayList<Integer> horiArrayListI=indicesArrayList.get(i);
            for(j=0; j<horiArrayListI.size(); j++){
                solarTermsArray[horiArrayListI.get(j)+1]=(char)(i+48);            
            } 
        }

        System.out.println("\n"+String.valueOf(solarTermsArray));

        int sum=0;

        for(i=0; i<indicesArrayList.size(); i++){
            System.out.printf("%5d: ",i);

            ArrayList<Integer> horiArrayListI=indicesArrayList.get(i);
            for(j=0; j<horiArrayListI.size(); j++){
            System.out.printf("%5d",horiArrayListI.get(j));
            
        }sum+=horiArrayListI.size();
        System.out.println();
        }

        sum=0;
        System.out.println("\n"+indicesArrayList.size()+"-------unique indices_______\n");  
        for(i=0; i<indicesArrayList.size(); i++){
            System.out.printf("%5d: ",i);

            ArrayList<Integer> horiArrayListI=indicesArrayList.get(i);
            for(j=0; j<horiArrayListI.size(); j++){
            System.out.printf("%5d",horiArrayListI.get(j));
            
        }sum+=horiArrayListI.size();
        System.out.println();
        }

        System.out.println("-------------------------------------------------");
        System.out.printf("sum=%d\n",sum);
        PrintWriter pw = new PrintWriter(new FileWriter("uniquesolarTerms.txt")) ;        

        for( i=0; i<69; i++){
            for( j=0; j<24; j++){
                pw.printf("%6d",NormalDateOfsolarTerms1900_2100Unique[i][j]);
            }
            pw.println();
        }
        pw.println();
         
        pw.close();
        int[] earliestSolarTermDays ={4,19,3,18,4,19,4,19,4,20,4,20,6,22,6,22,6,22,7,22,6,21,6,21};
        System.out.println("oooooooooooooooooooooooooooo");
        String segment;
        for( i=0; i<201; i++){
            int index=solarTermsArray[i]-48;
            segment=diff.substring(index*24,index*24+24);
            System.out.printf("%6d",i+1900);
            for( j=0; j<24; j++){
                int x=earliestSolarTermDays[j]+ Integer.parseInt(segment.substring(j,j+1));
                System.out.printf("%5d",x);
                 
            }
            System.out.println();
        }

    }
}


public class SolarTermParser2{

    /*
    cd healthyu
    javac -encoding utf-8 SolarTermParser.java
    java  SolarTermParser 
*/
    public static String[] SolarTerms24=
    {"小寒","大寒","立春","雨水","惊蛰","春分","清明","谷雨","立夏","小满","芒种","夏至","小暑","大暑","立秋","处暑","白露","秋分","寒露","霜降","立冬","小雪","大雪","冬至"};
    public static int[] earliestSolarTermDay ={4,19,3,18,4,19,4,19,4,20,4,20,6,22,6,22,6,22,7,22,6,21,6,21};

    public static String Leap="闰";
    public static String CNMonthLetter="月";
    public static char[] CNDayDigits={'一','二','三','四','五','六','七','八','九','十'};
    public static char[] CNMonthDigits={'初','十','二'};


    public static void main(String[] args) throws IOException {
        SolarTerms1900.parseSolarTerms1900("1900_24jieqi.txt");
        SolarTerm1901_2100.processSolarTerms1901_2100();
 
        SolarTerms1900_2100.createNormalDateOfsolarTermsMinMaxDiff();
        SolarTerms1900_2100.createNormalDateOfsolarTerms1900_2100Unique();
/*
        for(int i=0; i<200; i++){
            for(int j=0; j<24; j++){
                System.out.printf("%6d",DateNormalChineseData.normalDateOfsolarTerms[i][j]);
            }
            System.out.println();
        }
*/
    }          

  
}
