import java.net.*;
import java.io.*;
import javax.swing.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.File;//音楽再生時に必要
import javax.sound.sampled.AudioFormat;//音楽再生時に必要
import javax.sound.sampled.AudioSystem;//音楽再生時に必要
import javax.sound.sampled.Clip;//音楽再生時に必要
import javax.sound.sampled.DataLine;//音楽再生時に必要

public class MyClient extends JFrame implements MouseListener,MouseMotionListener,ActionListener {
	private JButton buttonArray[][],soundButton,resetButton;
	private Container c;
	private ImageIcon whiteIcon, blackIcon,blackIcon2, whiteIcon2, boardIcon, nextIcon,backgroundIcon,bturnIcon,wturnIcon,skipIcon,changeIcon;
	private int myColor,myTurn,actionCount,turnCount;
	private ImageIcon myIcon, yourIcon,myturnIcon,yourturnIcon,yourIconb,yourIconw,soundIcon,resetIcon,sendIcon;
	boolean changeflag;//,stopflag;
	private int stopflag;
	PrintWriter out;//出力用のライター
	JLabel theLabelA,theLabelB,actionLabel,turnLabel,yourIconLabel;
	
	
	//チャット画面関連
	JTextField tfKeyin;//メッセージ入力用テキストフィールド
	JTextArea taMain;//テキストエリア
	String myName;//名前を保存
	JButton bs,  actionButton;
	SoundPlayer theSoundPlayer;
	

	public MyClient() {
		//名前の入力ダイアログを開く
		String myName = JOptionPane.showInputDialog(null,"名前を入力してください","名前の入力",JOptionPane.QUESTION_MESSAGE);
		if(myName.equals("")){
			myName = "No name";//名前がないときは，"No name"とする
		}
		
		//IPアドレスのダイアログを開く
		String myipName = JOptionPane.showInputDialog(null,"IPアドレスを入力してください","名前の入力",JOptionPane.QUESTION_MESSAGE);
		if(myipName.equals("")){
			myipName = "localhost";//名前がないときは，"No name"とする
		}
		
	
		
		c = getContentPane();//フレームのペインを取得する
		c.setLayout(new FlowLayout());//レイアウトの設定
		
		//ウィンドウを作成する
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じるときに，正しく閉じるように設定する
		setTitle("MyClient");//ウィンドウのタイトルを設定する
		c.setPreferredSize(new Dimension(800,600));
		pack();
		
		//アイコンの設定
		blackIcon = new ImageIcon("black.png");
		whiteIcon = new ImageIcon("white.png");
		blackIcon2 = new ImageIcon("blackcount.png");
		whiteIcon2 = new ImageIcon("whitecount.png");
		boardIcon = new ImageIcon("boardIcon.png");
		nextIcon = new ImageIcon("nextIcon.png");
		bturnIcon = new ImageIcon("blackturn.png");
		wturnIcon = new ImageIcon("whiteturn.png");
		yourIconb = new ImageIcon("blackyouricon.png");
		yourIconw = new ImageIcon("whiteyouricon.png");
		skipIcon = new ImageIcon("skip.png");
		changeIcon = new ImageIcon("change.png");
		soundIcon = new ImageIcon("sound.png");
		resetIcon = new ImageIcon("reset.png");
		sendIcon = new ImageIcon("send.png");
		
		c.setLayout(null);//自動レイアウトの設定を行わない

		//チャット画面を作成する
		//入力用のテキストフィールドを作成
		tfKeyin = new JTextField("",42);
		c.add(tfKeyin);//コンテナに追加
        tfKeyin.setBounds(500,570,180,25);
		
		//送信ボタン
		bs= new JButton();
		c.add(bs);//ボタンをコンテナに追加
		bs.setIcon(sendIcon);
        bs.setBounds(680,570,50,25);
		bs.addActionListener(this);//ボタンを押したときの動作するようにする
		bs.addMouseListener(this);//ボタンをマウスでさわったときに反応するようにする
		bs.addMouseMotionListener(this);//ボタンをマウスで動かそうとしたときに反応するようにする
		bs.setActionCommand("Send");//ボタンに配列の情報を付加する（ネットワークを介してオブジェクトを識別するため）

		//チャットの出力用のフィールドを作成
		taMain = new JTextArea(20,50);
		c.add(taMain);//コンテナに追加
		taMain.setEditable(false);//編集不可にする
        taMain.setBounds(500,370,230,200);
		
		//soundボタン
		soundButton=new JButton();
		c.add(soundButton);//ボタンをコンテナに追加
		soundButton.setIcon(soundIcon);
        soundButton.setBounds(735,380,60,60);
		soundButton.addActionListener(this);//ボタンを押したときの動作するようにする
		soundButton.addMouseListener(this);//ボタンをマウスでさわったときに反応するようにする
		soundButton.addMouseMotionListener(this);//ボタンをマウスで動かそうとしたときに反応するようにする
		soundButton.setActionCommand("Sound");//ボタンに配列の情報を付加する（ネットワークを介してオブジェクトを識別するため）
		soundButton.setContentAreaFilled(false);
		
		//resetボタン
		resetButton=new JButton(resetIcon);
		c.add(resetButton);//ボタンをコンテナに追加
        resetButton.setBounds(735,450,60,60);
		resetButton.setActionCommand("Reset");
		resetButton.addActionListener(this);//ボタンを押したときの動作するようにする
		resetButton.addMouseListener(this);//ボタンをマウスでさわったときに反応するようにする
		resetButton.addMouseMotionListener(this);//ボタンをマウスで動かそうとしたときに反応するようにする
		resetButton.setContentAreaFilled(false);
		resetButton.setBorderPainted(false);
		
		//オセロボタンの生成
		buttonArray = new JButton[8][8];//ボタンの配列を５個作成する[0]から[4]まで使える
		for(int y=0;y<8;y++) {
			for(int x=0;x<8;x++) {
				buttonArray[y][x] = new JButton(boardIcon);//ボタンにアイコンを設定する
				c.add(buttonArray[y][x]);//ペインに貼り付ける
				buttonArray[y][x].setBounds(x*60,y*60+120,60,60);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
				buttonArray[y][x].addMouseListener(this);//ボタンをマウスでさわったときに反応するようにする
				buttonArray[y][x].addMouseMotionListener(this);//ボタンをマウスで動かそうとしたときに反応するようにする
				buttonArray[y][x].setActionCommand(Integer.toString(y*8+x));//ボタンに配列の情報を付加する（ネットワークを介してオブジェクトを識別するため）
				
			}
		}
		
		 //タイトルラベルの作成
		ImageIcon titleIcon = new ImageIcon("title.png");
		JLabel titleLabel = new JLabel(titleIcon);
		c.add(titleLabel);
		titleLabel.setBounds(5,10,475,98);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
		
		//自分の駒が何かを表示してくれるラベルの作成
		yourIconLabel = new JLabel(yourIconb);
		c.add(yourIconLabel);
		yourIconLabel.setBounds(500,10,238,60);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
		
		//ターンラベルの作成
		turnLabel = new JLabel(bturnIcon);
		c.add(turnLabel);
		turnLabel.setBounds(500,75,238,60);
		
		
		//結果表示ラベルの作成
		theLabelA = new JLabel("結果表示ラベル");
		theLabelB = new JLabel("結果表示ラベル");
		c.add(theLabelA);
		c.add(theLabelB);
		theLabelA.setBounds(545,320,45,45);
		theLabelB.setBounds(635,320,45,45);
		theLabelA.setFont(new Font("MS ゴシック",Font.BOLD,25));
		theLabelB.setFont(new Font("MS ゴシック",Font.BOLD,25));
		theLabelA.setText("×2");
		theLabelB.setText("×2");
		
        //画像付きラベルの作成
		JLabel whiteLabel = new JLabel(whiteIcon2);
        JLabel blackLabel = new JLabel(blackIcon2);
		c.add(whiteLabel);
		c.add(blackLabel);
		blackLabel.setBounds(500,320,45,45);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
        whiteLabel.setBounds(590,320,45,45);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
        //theLabel3.setForeground(Color.WHITE); //文字色の設定．Colorの設定は，このページを見て下さい　http://www.javadrive.jp/tutorial/color/
		
		 //必殺技が使用可能となるまでのカウントをしてくれるラベルの作成
		actionLabel = new JLabel("必殺技カウント");
		c.add(actionLabel);
		actionLabel.setBounds(500,295,280,20);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
		actionLabel.setText("必殺技使用可能まで10ターン");
		actionLabel.setFont(new Font("MS ゴシック",Font.BOLD,20));
       
		//必殺技ボタンの作成
		actionButton=new JButton();
		c.add(actionButton);//ボタンをコンテナに追加
		actionButton.setBounds(500,140,296,150);
		actionButton.addActionListener(this);//ボタンを押したときの動作するようにする
		actionButton.addMouseListener(this);//ボタンをマウスでさわったときに反応するようにする
		actionButton.addMouseMotionListener(this);//ボタンをマウスで動かそうとしたときに反応するようにする
		actionButton.setContentAreaFilled(false);
		
		//背景ラベルの作成
		ImageIcon backgroundIcon = new ImageIcon("background.png");
		JLabel backgroundLabel = new JLabel(backgroundIcon);
		c.add(backgroundLabel);
		backgroundLabel.setBounds(0,0,800,600);
		
		
		//サーバに接続する
		Socket socket = null;
		try {
			//"localhost"は，自分内部への接続．localhostを接続先のIP Address（"133.42.155.201"形式）に設定すると他のPCのサーバと通信できる
			//10000はポート番号．IP Addressで接続するPCを決めて，ポート番号でそのPC上動作するプログラムを特定する
			socket = new Socket(myipName, 10000);
		} catch (UnknownHostException e) {
			System.err.println("ホストの IP アドレスが判定できません: " + e);
		} catch (IOException e) {
			 System.err.println("エラーが発生しました: " + e);
		}
		
		MesgRecvThread mrt = new MesgRecvThread(socket, myName);//受信用のスレッドを作成する
		mrt.start();//スレッドを動かす（Runが動く）
		
	}
		
		
	//メッセージ受信のためのスレッド
	public class MesgRecvThread extends Thread {
		
		Socket socket;
		String myName;
		
		public MesgRecvThread(Socket s, String n){
			socket = s;
			myName = n;
		}
		
		//通信状況を監視し，受信データによって動作する
		public void run() {
			try{
				InputStreamReader sisr = new InputStreamReader(socket.getInputStream());
				BufferedReader br = new BufferedReader(sisr);
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println(myName);//接続の最初に名前を送る
				String myNumberStr = br.readLine();
				int myNumberInt = Integer.parseInt(myNumberStr);
				
				if(myNumberInt%2==0){
					myColor=0;
					myTurn=0;
					myIcon = whiteIcon;
					yourIcon = blackIcon;
					myturnIcon = wturnIcon;
					yourturnIcon = bturnIcon;
					actionButton.setActionCommand("skip");//ボタンに配列の情報を付加する（ネットワークを介してオブジェクトを識別するため）
					buttonArray[3][3].setIcon(yourIcon);
					buttonArray[4][4].setIcon(yourIcon);
					buttonArray[4][3].setIcon(myIcon);
					buttonArray[3][4].setIcon(myIcon);
					actionCount=0;
					turnCount=0;
					stopflag=0;
					yourIconLabel.setIcon(yourIconw);
					actionButton.setIcon(skipIcon);
					
				}else{
					myColor=1;
					myTurn=1;
					myIcon = blackIcon;
					yourIcon = whiteIcon;
					myturnIcon = bturnIcon;
					yourturnIcon = wturnIcon;
					actionButton.setActionCommand("change");//ボタンに配列の情報を付加する（ネットワークを介してオブジェクトを識別するため）
					buttonArray[3][3].setIcon(myIcon);
					buttonArray[4][4].setIcon(myIcon);
					buttonArray[4][3].setIcon(yourIcon);
					buttonArray[3][4].setIcon(yourIcon);
					actionCount=0;
					turnCount=0;
					checknextIcon();
					changeflag=false;
					stopflag=0;
					yourIconLabel.setIcon(yourIconb);
					actionButton.setIcon(changeIcon);
				}
				while(true) {
					String inputLine = br.readLine();//データを一行分だけ読み込んでみる
					if (inputLine != null) {//読み込んだときにデータが読み込まれたかどうかをチェックする
						System.out.println(inputLine);//デバッグ（動作確認用）にコンソールに出力する
						String[] inputTokens = inputLine.split(" ");	//入力データを解析するために、スペースで切り分ける
						String cmd = inputTokens[0];//コマンドの取り出し．１つ目の要素を取り出す
						
						if(cmd.equals("PLACE")){
							String theBName = inputTokens[1];
							int theBnum = Integer.parseInt(theBName);
							int y = theBnum/8;
							int x = theBnum%8;
							int theColor = Integer.parseInt(inputTokens[2]);
							if(theColor == myColor){
								//送信元クライアントでの処理
								buttonArray[y][x].setIcon(myIcon);
								turnLabel.setIcon(yourturnIcon);
							} else {
								//送信先クライアントでの処理
								buttonArray[y][x].setIcon(yourIcon);
								turnLabel.setIcon(myturnIcon);
							}
							myTurn=1-myTurn;
							
							if(myTurn==1){
								if(autoPass()){
									//サーバに情報を送る
									
									String msg = "PASS";
									out.println(msg);
									out.flush();			
								}else{
									checknextIcon();
								}
							}else{
								for(int q=0;q<8;q++){
									for(int p=0;p<8;p++){
										Icon theIcon = buttonArray[q][p].getIcon();
										if(theIcon==nextIcon){
											buttonArray[q][p].setIcon(boardIcon);
										}
									}
								}
							}
							int blackCount = 0;
							int whiteCount = 0;
							for(int q=0;q<8;q++){
								for(int p=0;p<8;p++){
									if(buttonArray[q][p].getIcon()==blackIcon){
										blackCount++;
									}
									if(buttonArray[q][p].getIcon()==whiteIcon){
										whiteCount++;
									}
								}
							}
							theLabelA.setText("×"+blackCount);
							theLabelB.setText("×"+whiteCount);
							turnCount++;
							if(turnCount<=10){
								int turnCount2 = 10-turnCount;
								actionLabel.setText("必殺技使用可能まで"+turnCount2+"ターン");
							}
							
						}
								
						if(cmd.equals("FLIP")){
							String theBName = inputTokens[1];
							int theBnum = Integer.parseInt(theBName);
							int y = theBnum/8;
							int x = theBnum%8;
							int theColor = Integer.parseInt(inputTokens[2]);
							if(theColor == myColor){
								//送信元クライアントでの処理
								buttonArray[y][x].setIcon(myIcon);
							} else {
								//送信先クライアントでの処理
								buttonArray[y][x].setIcon(yourIcon);
							}
						}
						
						if(cmd.equals("PASS")){
							myTurn=1-myTurn;
							checknextIcon();
							if(myTurn == 1){
								turnLabel.setIcon(myturnIcon);
							}else{
								turnLabel.setIcon(yourturnIcon);
							}
							if(myTurn==1){
								if(autoPass()){
									//サーバに情報を送る
									String msg = "JUDGE";
									out.println(msg);
									out.flush();			
								}
							}
						}
						//勝敗判定
						if(cmd.equals("JUDGE")){
							int myCount=0;
							int yourCount=0;
							
							for(int y=0;y<8;y++){
								for(int x=0;x<8;x++){
									if(buttonArray[y][x].getIcon()==myIcon){
										myCount++;
									}
									if(buttonArray[y][x].getIcon()==yourIcon){
										yourCount++;
									}
										
								}
							}
							if(myCount>yourCount){
								showJudgeWindow("win.png");
							}else if(myCount==yourCount){
								showJudgeWindow("draw.png");
							}else{
								showJudgeWindow("lose.png");
							}
									
							
							
						}
						//チャット
						if(cmd.equals("CHAT")){
							try{
								out = new PrintWriter(socket.getOutputStream(), true);
								out.println(myName);//接続の最初に名前を送る
								if (inputTokens[1] != null) {
									taMain.append(inputTokens[1]+"\n");//メッセージの内容を出力用テキストに追加する
								}
								else {
									break;
								}
								
							} catch (IOException e) {
								System.err.println("エラーが発生しました: " + e);
							}
						}
						
						//必殺技スキップ
						if(cmd.equals("SKIP")){
							myTurn = 1-myTurn;
							if(myTurn==1){
								if(autoPass()){
									//サーバに情報を送る
									String msg = "PASS";
									out.println(msg);
									out.flush();			
								}else{
									checknextIcon();
								}
							}else{
								for(int q=0;q<8;q++){
									for(int p=0;p<8;p++){
										Icon theIcon = buttonArray[q][p].getIcon();
										if(theIcon==nextIcon){
											buttonArray[q][p].setIcon(boardIcon);
										}
									}
								}
							}
						}
						
						//必殺技チェンジ
						if(cmd.equals("CHANGE")) {
							String theBName = inputTokens[1];
							int theBnum = Integer.parseInt(theBName);
							int y = theBnum/8;
							int x = theBnum%8;
							int theColor = Integer.parseInt(inputTokens[2]);
							if(theColor == myColor){
								buttonArray[y][x].setIcon(myIcon);
							}else{
								buttonArray[y][x].setIcon(yourIcon);
							}
							myTurn=1-myTurn;
							if(myTurn==1){
								if(autoPass()){
									//サーバに情報を送る
									String msg = "PASS";
									out.println(msg);
									out.flush();			
								}else{
									checknextIcon();
								}
							}else{
								for(int q=0;q<8;q++){
									for(int p=0;p<8;p++){
										Icon theIcon = buttonArray[q][p].getIcon();
										if(theIcon==nextIcon){
											buttonArray[q][p].setIcon(boardIcon);
										}
									}
								}
							}
						}
						
						//リセット
						if(cmd.equals("RESET")) {
							for(int y=0;y<8;y++) {
								for(int x=0;x<8;x++) {
									buttonArray[y][x].setIcon(boardIcon);
								}
							}
							taMain.setText("");//taMainのTextをクリアする
							actionLabel.setText("必殺技使用可能まで10ターン");
							theLabelA.setText("×2");
							theLabelB.setText("×2");
							if(myNumberInt%2==0){
								myColor=0;
								myTurn=0;
								buttonArray[3][3].setIcon(yourIcon);
								buttonArray[4][4].setIcon(yourIcon);
								buttonArray[4][3].setIcon(myIcon);
								buttonArray[3][4].setIcon(myIcon);
								actionCount=0;
								turnCount=0;
								stopflag=0;
								turnLabel.setIcon(yourturnIcon);
							}else{
								myColor=1;
								myTurn=1;
								actionButton.setActionCommand("change");//ボタンに配列の情報を付加する（ネットワークを介してオブジェクトを識別するため）
								buttonArray[3][3].setIcon(myIcon);
								buttonArray[4][4].setIcon(myIcon);
								buttonArray[4][3].setIcon(yourIcon);
								buttonArray[3][4].setIcon(yourIcon);
								actionCount=0;
								turnCount=0;
								checknextIcon();
								changeflag=false;
								stopflag=0;
								turnLabel.setIcon(myturnIcon);
							}
							
						}
					
					}else{
						break;
					}
				
				}
			} catch (IOException e) {
				System.err.println("エラーが発生しました: " + e);
			}
		}
	}

	public static void main(String[] args) {
		MyClient net = new MyClient();
		net.setVisible(true);
	}
  	
	public void mouseClicked(MouseEvent e) {//ボタンをクリックしたときの処理
		JButton theButton = (JButton)e.getComponent();//クリックしたオブジェクトを得る．型が違うのでキャストする
		String theArrayIndex = theButton.getActionCommand();//ボタンの配列の番号を取り出す
		Icon theIcon = theButton.getIcon();//theIconには，現在のボタンに設定されたアイコンが入る
		
		//必殺技スキップ。ターンを切り替える
		if(turnCount>=10){
			if(myTurn==0){
				if(actionCount==0){
					if(theArrayIndex.equals("skip")){
						String msg = "SKIP";
						out.println(msg);
						out.flush();
						actionCount++;
					}
				}
			}
		}
		//必殺技チェンジ。相手の駒を自分の駒にする
		if(turnCount>=10){
			if(myTurn==1){
				if(actionCount==0){
					if(theArrayIndex.equals("change")){
						changeflag=true;
						actionCount++;
					}
				}
			}
		}
		if(changeflag&&theIcon==yourIcon){
			String msg = "CHANGE"+" "+theArrayIndex+" "+myColor;
			out.println(msg);
			out.flush();
			changeflag=false;
		}
		// チャットのボタンが押されたら(Sendだったら）
		if(theArrayIndex.equals("Send")){
				String msg = "CHAT"+" "+tfKeyin.getText();//入力したテキストを得る
				if(tfKeyin.getText().length()>0){//入力したメッセージの長さが０で無ければ，
					tfKeyin.setText("");//tfKeyinのTextをクリアする
					out.println(msg);
					out.flush();
				}
			
		}
		
		//BGMボタンが押されたら音楽を鳴らす
		if(theArrayIndex.equals("Sound")&&stopflag==0){
            theSoundPlayer= new SoundPlayer("track_01.wav");
            theSoundPlayer.SetLoop(true);//ＢＧＭとして再生を繰り返す
            theSoundPlayer.play();
			stopflag++;
        }
		//BGMボタンが押されたら音楽を止める
		else if(theArrayIndex.equals("Sound")&&stopflag!=0){
			theSoundPlayer.stop();
			stopflag=0;
		}
		
		//リセットボタンが押されたら
		if(theArrayIndex.equals("Reset")){
			String msg = "RESET";
			out.println(msg);
			out.flush();
		}	
			
		// else オセロのボタンが押されたら
		else{
			if(myTurn==1){
				System.out.println(theIcon);//デバッグ（確認用）に，クリックしたアイコンの名前を出力する
				if(theIcon==boardIcon||theIcon==nextIcon) {
				
					/*if(myColor==0){//アイコンがwhiteIconと同じなら
						theButton.setIcon(blackIcon);//blackIconに設定する
					}
					if(myColor==1){
						theButton.setIcon(whiteIcon);//whiteIconに設定する
					}*/
					int temp = Integer.parseInt(theArrayIndex);
					int x = temp%8;
					int y = temp/8;
					if(judgeButton(y,x)){
						String msg = "PLACE" + " " + theArrayIndex + " " + myColor;
						out.println(msg);
						out.flush();
						repaint();//画面のオブジェクトを描画し直す
					}
					else{
						System.out.println("そこには配置はできません");
					}
				}
			}
		}
			
		
	}
	
	public void mouseEntered(MouseEvent e) {//マウスがオブジェクトに入ったときの処理
		//System.out.println("マウスが入った");
	}
	
	public void mouseExited(MouseEvent e) {//マウスがオブジェクトから出たときの処理
		//System.out.println("マウス脱出");
	}
	
	public void mousePressed(MouseEvent e) {//マウスでオブジェクトを押したときの処理（クリックとの違いに注意）
		//System.out.println("マウスを押した");
	}
	
	public void mouseReleased(MouseEvent e) {//マウスで押していたオブジェクトを離したときの処理
		//System.out.println("マウスを放した");
	}
	
	public void mouseDragged(MouseEvent e) {//マウスでオブジェクトとをドラッグしているときの処理
		
		/*System.out.println("マウスをドラッグ");
		JButton theButton = (JButton)e.getComponent();//型が違うのでキャストする
		String theArrayIndex = theButton.getActionCommand();//ボタンの配列の番号を取り出す
		Point theMLoc = e.getPoint();//発生元コンポーネントを基準とする相対座標
		System.out.println(theMLoc);//デバッグ（確認用）に，取得したマウスの位置をコンソールに出力する
		Point theBtnLocation = theButton.getLocation();//クリックしたボタンを座標を取得する
		theBtnLocation.x += theMLoc.x-15;//ボタンの真ん中当たりにマウスカーソルがくるように補正する
		theBtnLocation.y += theMLoc.y-15;//ボタンの真ん中当たりにマウスカーソルがくるように補正する
		theButton.setLocation(theBtnLocation);//マウスの位置にあわせてオブジェクトを移動する
		//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
		String msg = "MOVE"+" "+theArrayIndex+" "+theBtnLocation.x+" "+theBtnLocation.y;
		//サーバに情報を送る
		out.println(msg);//送信データをバッファに書き出す
		out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
		repaint();//オブジェクトの再描画を行う*/
	}

	public void mouseMoved(MouseEvent e) {//マウスがオブジェクト上で移動したときの処理
		/*System.out.println("マウス移動");
		int theMLocX = e.getX();//マウスのx座標を得る
		int theMLocY = e.getY();//マウスのy座標を得る
		System.out.println(theMLocX+","+theMLocY);//コンソールに出力する*/
	}
	
		//アクションが行われたときの処理
	public void actionPerformed(ActionEvent ae) {

  	}

	
	public boolean judgeButton(int y, int x){
		boolean flag = false;
		for(int j=-1;j<=1;j++){
			for(int i=-1;i<=1;i++){
				if(!(i==0&&j==0)){
					if(flipButtons(y,x,j,i)>=1){
						flag=true;
					}
				}
			}
		}
		return flag;
	}
	
	public int flipButtons(int y,int x,int j,int i){
		int flipNum = 0;
		int k;
		for(int dy=j,dx=i;;dy+=j,dx+=i){
			if((y+dy>7||y+dy<0)||(x+dx>7||x+dx<0)){
				return 0;
			}
			if(buttonArray[y+dy][x+dx].getIcon()==boardIcon||buttonArray[y+dy][x+dx].getIcon()==nextIcon){
				return 0;
			}		
			if(buttonArray[y+dy][x+dx].getIcon()==myIcon){
				for(dy=j, dx=i, k=0; k<flipNum; k++, dy+=j, dx+=i){
					//ボタンの位置情報を作る
					int msgy = y + dy;
					int msgx = x + dx;
					int theArrayIndex = msgy*8 + msgx;
				  
					//サーバに情報を送る
					String msg = "FLIP"+" "+theArrayIndex+" "+myColor;
					out.println(msg);
					out.flush();
				}
					return flipNum;
			}
			if(buttonArray[y+dy][x+dx].getIcon()==yourIcon){
				flipNum+=1;
			}
			
				
		}
	}
	
	//パスするかどうかを判定してくれる。置けない（パス）のときtrueを返す。
	public boolean autoPass(){
		boolean flag = true;
		for (int y=0;y<8;y++){
			for(int x=0;x<8;x++){
				if(buttonArray[y][x].getIcon()==boardIcon||buttonArray[y][x].getIcon()==nextIcon){
					for(int j=-1;j<=1;j++){
						for(int i=-1;i<=1;i++){
							if(!(i==0&&j==0)){
								if(flipButtons2(y,x,j,i)>=1){
									return false;
								}
							}
						}
					}
				}
			}
		}
		return flag;
	}
	
	//駒がおけるか置けないかサーチ
	public int flipButtons2(int y,int x,int j,int i){
		int flipNum = 0;
		for(int dy=j,dx=i;;dy+=j,dx+=i){
			if((y+dy>7||y+dy<0)||(x+dx>7||x+dx<0)){
				return 0;
			}
			if(buttonArray[y+dy][x+dx].getIcon()==boardIcon||buttonArray[y+dy][x+dx].getIcon()==nextIcon){
				return 0;
			}		
			if(buttonArray[y+dy][x+dx].getIcon()==myIcon){
				return flipNum;
			}
			if(buttonArray[y+dy][x+dx].getIcon()==yourIcon){
				flipNum+=1;
			}
			
				
		}
	}	
	
	//マップ機能。次にどこに置いたら良いのかを表示してくれる。
	public void checknextIcon(){
		for(int q=0;q<8;q++){
			for(int p=0;p<8;p++){
				if(buttonArray[q][p].getIcon()==boardIcon){
					for(int j=-1;j<=1;j++){
						for(int i=-1;i<=1;i++){
							if(!(i==0&&j==0)){
								if(flipButtons2(q,p,j,i)>=1){
									buttonArray[q][p].setIcon(nextIcon);
								}
							}
						}
					}
				}
			}
		}	
	}
	
	//勝敗判定のダイアログを表示してくれる。
	public void showJudgeWindow(String str){
		DialogWindow dlg = new DialogWindow(this,str);
		setVisible(true);
	}
	
	//BGMに関するクラス
    public class SoundPlayer{
        private AudioFormat format = null;
        private DataLine.Info info = null;
        private Clip clip = null;
        boolean stopFlag = false;
        Thread soundThread = null;
        private boolean loopFlag = false;

        public SoundPlayer(String pathname){
            File file = new File(pathname);
            try{
                format = AudioSystem.getAudioFileFormat(file).getFormat();
                info = new DataLine.Info(Clip.class, format);
                clip = (Clip) AudioSystem.getLine(info);
                clip.open(AudioSystem.getAudioInputStream(file));
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        public void SetLoop(boolean flag){
            loopFlag = flag;
        }

        public void play(){
            soundThread = new Thread(){
                public void run(){
                    long time = (long)clip.getFrameLength();//44100で割ると再生時間（秒）がでる
                    System.out.println("PlaySound time="+time);
                    long endTime = System.currentTimeMillis()+time*1000/44100;
                    clip.start();
                    System.out.println("PlaySound time="+(int)(time/44100));
                    while(true){
                        if(stopFlag){//stopFlagがtrueになった終了
                            System.out.println("PlaySound stop by stopFlag");
                            clip.stop();
                            return;
                        }
                        //System.out.println("endTime="+endTime);
                        //System.out.println("currentTimeMillis="+System.currentTimeMillis());
                        if(endTime < System.currentTimeMillis()){//曲の長さを過ぎたら終了
                            System.out.println("PlaySound stop by sound length");
                            if(loopFlag) {
                                clip.loop(1);//無限ループとなる
                            } else {
                                clip.stop();
                                return;
                            }
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            soundThread.start();
        }

        public void stop(){
            stopFlag = true;
            System.out.println("StopSound");
        }

    }	
}
//ダイアログのためのクラス
//絵をクリックしたら閉じるようにしている
class DialogWindow extends JDialog implements ActionListener{
		DialogWindow(JFrame owner,String str) {
        super(owner);//呼び出しもととの親子関係の設定．これをコメントアウトすると別々のダイアログになる

		Container c = this.getContentPane();	//フレームのペインを取得する
        c.setLayout(null);		//自動レイアウトの設定を行わない

        JButton theButton = new JButton();//画像を貼り付けるラベル
        ImageIcon theImage = new ImageIcon(str);//なにか画像ファイルをダウンロードしておく
        theButton.setIcon(theImage);//ラベルを設定
        theButton.setBounds(0,0,600,258);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
        theButton.addActionListener(this);//ボタンをクリックしたときにactionPerformedで受け取るため
        c.add(theButton);//ダイアログに貼り付ける（貼り付けないと表示されない
        setTitle("");//タイトルの設定
        setSize(600,258);//大きさの設定
        setResizable(false);//拡大縮小禁止//trueにすると拡大縮小できるようになる
        setUndecorated(true);//タイトルを表示しない
        setModal(true);//上を閉じるまで下を触れなくする（falseにすると触れる）

        //ダイアログの大きさや表示場所を変更できる
        //親のダイアログの中心に表示したい場合は，親のウィンドウの中心座標を求めて，子のダイアログの大きさの半分ずらす
        setLocation(owner.getBounds().x+owner.getWidth()/2-this.getWidth()/2,owner.getBounds().y+owner.getHeight()/2-this.getHeight()/2);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        this.dispose();//Dialogを廃棄する
    }
}






