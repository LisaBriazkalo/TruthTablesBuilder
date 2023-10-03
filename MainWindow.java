import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class MainWindow extends JFrame{
    private JTextField function;
    private JLabel name;
    private JButton denial;
    private JButton conjunction;
    private JButton disjunction;
    private JButton implication;
    private JButton equivalence;
    private JButton Build;
    private JPanel panel;
    private JPanel pn;
    int p=0;
    char[] arr = new char[10];
    String toPost(String text){
        int n = 0;
        String result = "";
        stackClear();
        while(n<text.length()){
            if(!isArgument(text.charAt(n))&&text.charAt(n)!='¬'&&text.charAt(n)!='('&&text.charAt(n)!=')'){
                int priority = operationPriority(text.charAt(n));
                while(p>0 && arr[p-1]!='(' && operationPriority(arr[p-1])>=priority){
                    result+=pop();
                }
                push(text.charAt(n));
            }
            if(isArgument(text.charAt(n))){
                int denial=0, i=n;
                while(i>0){
                    if(text.charAt(i)==')')
                        break;
                    if(text.charAt(i)=='('){
                        if(text.charAt(i-1)=='¬'){
                            result+='¬';
                            denial=1;
                            i=0;
                        }
                    }
                    i--;
                }
                i=n-1;
                while(i>=0 && n>0 && denial==0 && text.charAt(i)=='¬'){
                    result+='¬';
                    i--;
                }
                result+=text.charAt(n);
            }
            if(text.charAt(n)=='('){
                push('(');
            }
            if(text.charAt(n)==')'){
                char operation = pop();
                //String operation=")";
                while(operation!='('){
                    if(p <0){
                        return "-1";
                    }
                    result += operation;
                    operation = pop();
                }
            }
            n++;
        }
        while(p>0){
            result += pop();
        }
        String strNew = result.replaceAll("¬¬", "");
        System.out.print("result="+strNew+"\n");
        result=strNew;
        return result;
    }
    int operationPriority(char c){
        if(c=='⇔'){
            return 1;
        }
        if(c=='→'){
            return 2;
        }
        if(c=='∨'||c=='|'){
            return 3;
        }
        if(c=='∧'||c=='&'){
            return 4;
        }
        if(c=='!'||c=='¬'){
            return 5;
        }
        return 0;
    }
    Boolean isArgument(char c){
        if(c>='a'&&c<='z'){
            return true;
        }
        if(c>='A'&&c<='Z'){
            return true;
        }
        if(c>='0'&&c<='9'){
            return true;
        }
        return false;
    }
    void push(char item){//Вставка елемента в стек
        arr[p] = item;
        p++;
    }
    char pop(){//Отримання елемента зі стеку
        p--;
        return arr[p];
    }
    void stackClear(){//Повна очистка стека
        p = 0;
    }
    String execOperation(char a,char b,char op){
        if(op=='&'||op=='∧'){
            if(a=='1'&&b=='1'){
                return "1";
            }else{
                return "0";
            }
        }
        if(op=='|'||op=='∨'){
            if(a=='1'||b=='1'){
                return "1";
            }else{
                return "0";
            }
        }
        if(op=='→'){
            Boolean aa=false, bb=false;
            if(a=='1')  aa = true;
            if(b=='1')  bb = true;
            if(!(aa)|bb){
                return "1";
            }
            else {
                return "0";
            }
        }
        if(op=='⇔'){
            Boolean aa=false, bb=false;
            if(a=='1')  aa = true;
            if(b=='1')  bb = true;
            if(!(aa^bb)){
                return "1";
            }else{
                return "0";
            }
        }
        return "x";
    }
    char solve(String expr){
        stackClear();
        int n = 0;
        while(n<expr.length()){
            if(isArgument(expr.charAt(n))){
                push(expr.charAt(n));
            }
            else if(expr.charAt(n)=='!'||expr.charAt(n)=='¬'){
                n++;
                push(expr.charAt(n));
                char a = pop();
                if(a=='0'){
                    push('1');
                }else{
                    push('0');
                }
            }
            else{
                char b = pop();
                char a = pop();
                String c = execOperation(a,b,expr.charAt(n));
                push(c.charAt(0));
            }
            n++;
        }
        if(p==1){
            return pop();
        }
        else{
          return '-';
        }
    }
    Boolean builtTruthTable(String postfix, String args){//Побудова таблиці істиності
        int count=0;
        Label[] table=new Label[(int)Math.pow(2,args.length())*(args.length()+1)+args.length()+1];
        for(int i=0;i<table.length;i++) {
            table[i] = new Label();
        }
        Boolean[][] arr = new Boolean[(int)Math.pow(2,args.length())][args.length()+1];
        for(int j=0;j<args.length();j++){
            double k=Math.pow(2,args.length())/Math.pow(2,(j+1));
            int i=(int)k;
            int t=2*(int)k;
            while(i<(int)Math.pow(2,args.length())){
                while(i<t) {
                    arr[i][j]=true; i++;
                }
                t+=2*k; i+=k;
            }
        }
        for(int i=0; i<(int)Math.pow(2,args.length()); i++){
            for(int j=0;j<args.length();j++){
                if (arr[i][j]==null)
                    arr[i][j]=false;
            }
        }
        for(int i=0;i<=args.length();i++){
            table[count].setBounds(30*i,0,30,30);
            if(i<args.length())table[i].setText(Character.toString(args.charAt(i)));
            table[args.length()].setText("f");
            table[count].setBackground(new Color(218, 247, 120 ));
            table[count].setAlignment(Label.CENTER);
            pn.add(table[count]);
            count++;
        }
        for(int i=0;i<(int)Math.pow(2,args.length());i++){
            for(int j=0;j<args.length();j++){
                table[count].setBounds(30*j,30+30*i,30,30);
                if(arr[i][j]==true)
                    table[count].setText("1");
                else
                    table[count].setText("0");
                if(arr[i][j]==true)
                    table[count].setBackground(new Color(218, 247, 166 ));
                else
                    table[count].setBackground(Color.white);
                table[count].setAlignment(Label.CENTER);
                pn.add(table[count]);
                pn.setBackground(panel.getBackground());
                count++;
            }
        }

        for (int i = 0; i < Math.pow(2,args.length()); i++) {
            String tempT = postfix;
            for (var j = 0; j < args.length(); j++) {
                String a="";
                if (arr[i][j]==false)  a="0";
                if (arr[i][j]==true)  a="1";
                tempT = tempT.replaceAll(Character.toString(args.charAt(j)),a);
            }
            //System.out.print(tempT+"\n");
            char result = solve(tempT);
            table[count].setBounds(30*args.length(),30+30*i,30,30);
            table[count].setText(Character.toString(result));
            if(result=='1')
                table[count].setBackground(new Color(218, 247, 166 ));
            else
                table[count].setBackground(Color.white);
            table[count].setAlignment(Label.CENTER);
            pn.add(table[count]);
            pn.setBackground(panel.getBackground());
            count++;
            if(result == '-'){
                return false;
            }
        }
        return true;
    }
    Boolean builtKarno(String postfix, String args) {//Побудова та мінімцізація картами Карно
        int lenX = 0, lenY = 0;
        if (args.length() % 2 == 0) {
            lenX = args.length();
            lenY = args.length();
        }
        if (args.length() % 2 != 0) {
            lenX = args.length() + 1;
            lenY = lenX/2;
        }
        String[] arX = new String[lenX];
        String[] arY = new String[lenY];
        String set = new String("");
        if (args.length() == 1) {
            arX[0] = "0";
            arX[1] = "1";
            //arY[0] = "";
        } else if (args.length() == 2) {
            arX[0] = "0";
            arX[1] = "1";
            arY[0] = "0";
            arY[1] = "1";
        } else if (args.length() == 3) {
            arX[0] = "00";
            arX[1] = "01";
            arX[2] = "11";
            arX[3] = "10";
            arY[0] = "0";
            arY[1] = "1";
        } else if (args.length() == 4) {
            arX[0] = "00";
            arX[1] = "01";
            arX[2] = "11";
            arX[3] = "10";
            arY[0] = "00";
            arY[1] = "01";
            arY[2] = "11";
            arY[3] = "10";

        }
        int count = 0;
        Label[] table = new Label[(arX.length+1) * (arY.length+1)];
        for (int i = 0; i < table.length; i++) {
            table[i] = new Label();
        }

        table[count].setBounds(210, 0, 30, 30);
        if(args.length()==1)table[count].setText(Character.toString(args.charAt(0)));
        if(args.length()==2)table[count].setText(Character.toString(args.charAt(0))+"/"+Character.toString(args.charAt(1)));
        else if(args.length()==3)table[count].setText(Character.toString(args.charAt(0))+Character.toString(args.charAt(1))+"/"+Character.toString(args.charAt(2)));
        else if(args.length()==4)table[count].setText(Character.toString(args.charAt(0))+Character.toString(args.charAt(1))+"/"+Character.toString(args.charAt(2))+Character.toString(args.charAt(3)));
        table[count].setBackground(new Color(218, 247, 120));
        table[count].setAlignment(Label.CENTER);
        pn.add(table[count]);
        count++;

        for (var i = 0; i < arX.length; i++) {
            table[count].setBounds(240 + 30 * i, 0, 30, 30);
            table[count].setText(arX[i]);
            table[count].setBackground(new Color(218, 247, 120));
            table[count].setAlignment(Label.CENTER);
            pn.add(table[count]);
            count++;
        }

        for (var i = 0; i < arY.length; i++) {
            table[count].setBounds(210, 30 + 30 * i, 30, 30);
            table[count].setText(arY[i]);
            table[count].setBackground(new Color(218, 247, 120));
            table[count].setAlignment(Label.CENTER);
            pn.add(table[count]);
            count++;
            for (int j = 0; j < arX.length; j++) {
                char[] change = new char[args.length()];
                if (arX.length == 2) {
                    change[0] = arX[j].charAt(0);
                    if (arY.length == 2) {
                        change[1] = arY[i].charAt(0);
                    }
                } else {
                    change[0] = arX[j].charAt(0);
                    change[1] = arX[j].charAt(1);
                    if (arY.length == 2) {
                        change[2] = arY[i].charAt(0);
                    } else {
                        change[2] = arY[i].charAt(0);
                        change[3] = arY[i].charAt(1);
                    }
                }
                String tempT = postfix;
                for (int k = 0; k < change.length; k++) {
                    tempT = tempT.replaceAll(Character.toString(args.charAt(k)), Character.toString(change[k]));
                }
                char result = solve(tempT);
                if (result == '-') {
                    return false;
                }
                if (result == '1') {
                    for(int k=0;k<change.length;k++){
                        set=set+Integer.toString(change[k])+" ";
                    }
                    table[count].setBounds(240 + 30 * j, 30 + 30 * i, 30, 30);
                    table[count].setText("1");
                    table[count].setBackground(Color.white);
                    table[count].setAlignment(Label.CENTER);
                    pn.add(table[count]);
                    count++;
                }
                else {
                    table[count].setBounds(240 + 30 * j, 30 + 30 * i, 30, 30);
                    table[count].setText("0");
                    table[count].setBackground(Color.white);
                    table[count].setAlignment(Label.CENTER);
                    pn.add(table[count]);
                    count++;
                }
            }
        }
        int r_color=200, g_color=255, b_color=10;
        String result="";
        for(int k=lenX+2; k<table.length; k++){
            if(k%(lenX+1)==0) k++;
            int start=k;
            int st=k;
            if(table[k].getText()=="1" && table[k].getBackground()==Color.white ){
                if(k%3==0) r_color-=30;
                if(k%3==1) g_color-=65;
                else b_color+=60;
                System.out.print("start="+start+"\n");
                int y=0, height=0, weight=1;
                while(k+y*(lenX+1)<table.length && table[k+y*(lenX+1)].getText()=="1"){
                    y++;
                    height++;
                }
                if(k-(lenX+1)>lenX+1 && table[k-(lenX+1)].getText()=="1"){
                    y=-1;
                    while(k+y*(lenX+1)<table.length && table[k+y*(lenX+1)].getText()=="1"){
                        st=k+y*(lenX+1);
                        y--;
                        height++;
                    }
                }
                int move=0;
                if(height==1 && k<2*(lenX+1) && args.length()==4){
                    if(table[k+3*(lenX+1)].getText()=="1") {
                        height++;
                        move++;
                    }
                }
                if(height==3)
                    height=2;
                y=0;
                if(k+lenX+1>table.length && height>1)
                    st=k-(lenX+1);
                y=0;
                int rn=1;
                int is_one_right=0;
                while((st+y*(lenX+1)+rn)%(lenX+1)!=0){
                    y=0;
                    is_one_right=1;
                    if (st+y*(lenX+1)+rn>=table.length || table[st+y*(lenX+1)+rn].getText()!="1") is_one_right=0;
                    while(st+y*(lenX+1)+rn<table.length && y<height) {
                       if(table[st+y*(lenX+1)+rn].getText()!="1")
                           is_one_right=0;
                        if(move==0) y++;
                        if(move==1) y=3;
                    }
                    if(is_one_right==1) rn++;
                    else break;
                }
                y=0;
                int ln=-1;
                int is_one_left=0;
                while((st+y*(lenX+1)+ln)%(lenX+1)!=0){
                    y=0;
                    is_one_left=1;
                    if (table[st+y*(lenX+1)+ln].getText()!="1") is_one_left=0;
                    while(y<height && st+y*(lenX+1)+ln<table.length) {
                        int t=st+y*(lenX+1)+ln;
                        if(table[st+y*(lenX+1)+ln].getText()!="1")
                            is_one_left=0;
                        if(move==0) y++;
                        if(move==1) y=3;
                    }
                    if(is_one_left==1) ln--;
                    else break;
                }
                ln=-ln-1;
                rn-=1;
                weight=ln+rn+1;
                if(weight==3){
                    if(ln==2){
                        weight=2;
                        ln=1;
                    }
                    else{
                        weight=2;
                    }
                }
                System.out.print("height="+height+" weight="+weight+"\n");
                String[] rw=new String[weight];
                String[] rh=new String[height];
                String result_w = new String("");
                String result_h = new String("");
                if(args.length()==2){
                    for(int t=0; t<height;t++){
                        int res=(st+t*(lenX+1)-1)/(lenX+1);
                        rh[t]=two_elm(res,args,1);
                    }
                    for(int j=0;j<weight;j++){
                        int res=(st+j-ln)%(lenX+1);
                        rw[j]=two_elm(res,args,0);
                    }
                }
                if(args.length()==3){
                    for(int t=0; t<height;t++){
                        int res=(st+t*(lenX+1)-1)/(lenX+1);
                        rh[t]=two_elm(res,args,2);
                    }
                    for(int j=0;j<weight;j++){
                        int res=(st+j-ln)%(lenX+1);
                        rw[j]=four_elm(res,args,0,1);
                    }
                }
                if(args.length()==4){
                    for(int t=0; t<height;t++){
                        int res=(st+t*(lenX+1)-1)/(lenX+1);
                        rh[t]=four_elm(res,args,2,3);
                    }
                    if(move==1){
                        int res=(st+3*(lenX+1)-1)/(lenX+1);
                        rh[1]=four_elm(res,args,2,3);
                    }
                    for(int j=0;j<weight;j++){
                        int res=(st+j-ln)%(lenX+1);
                        rw[j]=four_elm(res,args,0,1);
                    }
                }
                result_w=min(rw,weight,move);
                result_h=min(rh,height,move);
                //           (a∧b)∨(c∧d)
                if(move==1){
                    for(int n=0; n<weight; n++){
                        table[st-ln+n].setBackground(new Color(r_color, g_color, b_color ));
                        table[st-ln+3*(lenX+1)+n].setBackground(new Color(r_color, g_color, b_color ));
                    }
                }
                else{
                    for(int j=0; j<height; j++){
                        for(int n=0; n<weight; n++){
                            table[st-ln+j*(lenX+1)+n].setBackground(new Color(r_color, g_color, b_color ));
                        }
                    }
                }
                result=result+result_w+result_h+"+";
            }
        }

        char[] myChars = result.toCharArray();
        myChars[myChars.length-1] = ' ';
        result = String.valueOf(myChars);
        System.out.print("RESULT="+result+"\n");
        Label resultLabel = new Label();
        resultLabel.setBounds(220, 160,200, 20);
        resultLabel.setText("f(");
        for(int i=0; i<args.length();i++){
            if(i==args.length()-1) resultLabel.setText(resultLabel.getText()+args.charAt(i)+")="+result);
            else resultLabel.setText(resultLabel.getText()+args.charAt(i)+", ");
        }
        pn.add(resultLabel);
    return true;
    }
    String two_elm(int res, String args, int el){
        String r = new String();
        if(res==1) r="¬"+Character.toString(args.charAt(el));
        else if(res==2) r=Character.toString(args.charAt(el));
        return r;
    }
    String four_elm(int res, String args, int el1,int el2){
        String r = new String();
        if(res==1) r="¬"+Character.toString(args.charAt(el1))+"¬"+Character.toString(args.charAt(el2));
        else if(res==2) r="¬"+Character.toString(args.charAt(el1))+Character.toString(args.charAt(el2));
        else if(res==3) r=Character.toString(args.charAt(el1))+Character.toString(args.charAt(el2));
        else if(res==4) r=Character.toString(args.charAt(el1))+"¬"+Character.toString(args.charAt(el2));
        return r;
    }
    String min(String[] rh, int height, int move){
        String current = new String("");
        String result=new String("");
        if(height==1) result=rh[0];
        if(height!=4){
            current+=rh[0];
            for(int j=1;j<height;j++){
                for(int p=0;p<current.length();p++){
                    for(int t=0;t<rh[j].length();t++){
                        if(current.charAt(p)=='¬')
                            p++;
                        if(current.charAt(p)==rh[j].charAt(t)){
                            if(p!=0 && t!=0 && current.charAt(p-1)=='¬' && rh[j].charAt(t-1)=='¬'){
                                result+="¬"+rh[j].charAt(t);
                            }
                            else if(p==0 && t==0)
                                result+=rh[j].charAt(t);
                            else if( current.charAt(p-1)!='¬' && rh[j].charAt(t-1)!='¬')
                                result+=rh[j].charAt(t);
                            else p++;
                        }
                    }
                        current=result;
                }
            }
        }
        return result;
    }
    public MainWindow() {
        setContentPane(panel);
        pn.setLayout(null);
        setTitle("Побудова таблиці істинності. Мінімізація булевих функцій методом карт Карно ");
        setSize(630,700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        Build.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pn.removeAll();
                String postfix = toPost(function.getText());
                String args = "";
                for (int i = 0; i < postfix.length(); i++) {
                    if(isArgument(postfix.charAt(i))){
                        if(!args.contains(Character.toString(postfix.charAt(i)))){
                            args+=postfix.charAt(i);
                        }
                    }
                }
                char arr[] = args.toCharArray();
                char temp;
                int i = 0;
                while (i < arr.length) {
                    int j = i + 1;
                    while (j < arr.length) {
                        if (arr[j] < arr[i]) {
                            temp = arr[i];
                            arr[i] = arr[j];
                            arr[j] = temp;
                        }
                        j += 1;
                    }
                    i += 1;
                }
                args=String.valueOf(arr);
                Boolean k=builtTruthTable(postfix,args);
                k=builtKarno(postfix,args);
            }
        });
        conjunction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {function.setText(function.getText()+"∧");}
        });
        implication.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {function.setText(function.getText()+"→");}
        });
        denial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {function.setText(function.getText()+"¬");}
        });
        disjunction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {function.setText(function.getText()+"∨");}
        });
        equivalence.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {function.setText(function.getText()+"⇔");}
        });
    }
}
