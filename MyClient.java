import java.net.*;
import java.io.*;
import javax.swing.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.File;//���y�Đ����ɕK�v
import javax.sound.sampled.AudioFormat;//���y�Đ����ɕK�v
import javax.sound.sampled.AudioSystem;//���y�Đ����ɕK�v
import javax.sound.sampled.Clip;//���y�Đ����ɕK�v
import javax.sound.sampled.DataLine;//���y�Đ����ɕK�v

public class MyClient extends JFrame implements MouseListener,MouseMotionListener,ActionListener {
	private JButton buttonArray[][],soundButton,resetButton;
	private Container c;
	private ImageIcon whiteIcon, blackIcon,blackIcon2, whiteIcon2, boardIcon, nextIcon,backgroundIcon,bturnIcon,wturnIcon,skipIcon,changeIcon;
	private int myColor,myTurn,actionCount,turnCount;
	private ImageIcon myIcon, yourIcon,myturnIcon,yourturnIcon,yourIconb,yourIconw,soundIcon,resetIcon,sendIcon;
	boolean changeflag;//,stopflag;
	private int stopflag;
	PrintWriter out;//�o�͗p�̃��C�^�[
	JLabel theLabelA,theLabelB,actionLabel,turnLabel,yourIconLabel;
	
	
	//�`���b�g��ʊ֘A
	JTextField tfKeyin;//���b�Z�[�W���͗p�e�L�X�g�t�B�[���h
	JTextArea taMain;//�e�L�X�g�G���A
	String myName;//���O��ۑ�
	JButton bs,  actionButton;
	SoundPlayer theSoundPlayer;
	

	public MyClient() {
		//���O�̓��̓_�C�A���O���J��
		String myName = JOptionPane.showInputDialog(null,"���O����͂��Ă�������","���O�̓���",JOptionPane.QUESTION_MESSAGE);
		if(myName.equals("")){
			myName = "No name";//���O���Ȃ��Ƃ��́C"No name"�Ƃ���
		}
		
		//IP�A�h���X�̃_�C�A���O���J��
		String myipName = JOptionPane.showInputDialog(null,"IP�A�h���X����͂��Ă�������","���O�̓���",JOptionPane.QUESTION_MESSAGE);
		if(myipName.equals("")){
			myipName = "localhost";//���O���Ȃ��Ƃ��́C"No name"�Ƃ���
		}
		
	
		
		c = getContentPane();//�t���[���̃y�C�����擾����
		c.setLayout(new FlowLayout());//���C�A�E�g�̐ݒ�
		
		//�E�B���h�E���쐬����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//�E�B���h�E�����Ƃ��ɁC����������悤�ɐݒ肷��
		setTitle("MyClient");//�E�B���h�E�̃^�C�g����ݒ肷��
		c.setPreferredSize(new Dimension(800,600));
		pack();
		
		//�A�C�R���̐ݒ�
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
		
		c.setLayout(null);//�������C�A�E�g�̐ݒ���s��Ȃ�

		//�`���b�g��ʂ��쐬����
		//���͗p�̃e�L�X�g�t�B�[���h���쐬
		tfKeyin = new JTextField("",42);
		c.add(tfKeyin);//�R���e�i�ɒǉ�
        tfKeyin.setBounds(500,570,180,25);
		
		//���M�{�^��
		bs= new JButton();
		c.add(bs);//�{�^�����R���e�i�ɒǉ�
		bs.setIcon(sendIcon);
        bs.setBounds(680,570,50,25);
		bs.addActionListener(this);//�{�^�����������Ƃ��̓��삷��悤�ɂ���
		bs.addMouseListener(this);//�{�^�����}�E�X�ł�������Ƃ��ɔ�������悤�ɂ���
		bs.addMouseMotionListener(this);//�{�^�����}�E�X�œ��������Ƃ����Ƃ��ɔ�������悤�ɂ���
		bs.setActionCommand("Send");//�{�^���ɔz��̏���t������i�l�b�g���[�N����ăI�u�W�F�N�g�����ʂ��邽�߁j

		//�`���b�g�̏o�͗p�̃t�B�[���h���쐬
		taMain = new JTextArea(20,50);
		c.add(taMain);//�R���e�i�ɒǉ�
		taMain.setEditable(false);//�ҏW�s�ɂ���
        taMain.setBounds(500,370,230,200);
		
		//sound�{�^��
		soundButton=new JButton();
		c.add(soundButton);//�{�^�����R���e�i�ɒǉ�
		soundButton.setIcon(soundIcon);
        soundButton.setBounds(735,380,60,60);
		soundButton.addActionListener(this);//�{�^�����������Ƃ��̓��삷��悤�ɂ���
		soundButton.addMouseListener(this);//�{�^�����}�E�X�ł�������Ƃ��ɔ�������悤�ɂ���
		soundButton.addMouseMotionListener(this);//�{�^�����}�E�X�œ��������Ƃ����Ƃ��ɔ�������悤�ɂ���
		soundButton.setActionCommand("Sound");//�{�^���ɔz��̏���t������i�l�b�g���[�N����ăI�u�W�F�N�g�����ʂ��邽�߁j
		soundButton.setContentAreaFilled(false);
		
		//reset�{�^��
		resetButton=new JButton(resetIcon);
		c.add(resetButton);//�{�^�����R���e�i�ɒǉ�
        resetButton.setBounds(735,450,60,60);
		resetButton.setActionCommand("Reset");
		resetButton.addActionListener(this);//�{�^�����������Ƃ��̓��삷��悤�ɂ���
		resetButton.addMouseListener(this);//�{�^�����}�E�X�ł�������Ƃ��ɔ�������悤�ɂ���
		resetButton.addMouseMotionListener(this);//�{�^�����}�E�X�œ��������Ƃ����Ƃ��ɔ�������悤�ɂ���
		resetButton.setContentAreaFilled(false);
		resetButton.setBorderPainted(false);
		
		//�I�Z���{�^���̐���
		buttonArray = new JButton[8][8];//�{�^���̔z����T�쐬����[0]����[4]�܂Ŏg����
		for(int y=0;y<8;y++) {
			for(int x=0;x<8;x++) {
				buttonArray[y][x] = new JButton(boardIcon);//�{�^���ɃA�C�R����ݒ肷��
				c.add(buttonArray[y][x]);//�y�C���ɓ\��t����
				buttonArray[y][x].setBounds(x*60,y*60+120,60,60);//�{�^���̑傫���ƈʒu��ݒ肷��D(x���W�Cy���W,x�̕�,y�̕��j
				buttonArray[y][x].addMouseListener(this);//�{�^�����}�E�X�ł�������Ƃ��ɔ�������悤�ɂ���
				buttonArray[y][x].addMouseMotionListener(this);//�{�^�����}�E�X�œ��������Ƃ����Ƃ��ɔ�������悤�ɂ���
				buttonArray[y][x].setActionCommand(Integer.toString(y*8+x));//�{�^���ɔz��̏���t������i�l�b�g���[�N����ăI�u�W�F�N�g�����ʂ��邽�߁j
				
			}
		}
		
		 //�^�C�g�����x���̍쐬
		ImageIcon titleIcon = new ImageIcon("title.png");
		JLabel titleLabel = new JLabel(titleIcon);
		c.add(titleLabel);
		titleLabel.setBounds(5,10,475,98);//�{�^���̑傫���ƈʒu��ݒ肷��D(x���W�Cy���W,x�̕�,y�̕��j
		
		//�����̋������\�����Ă���郉�x���̍쐬
		yourIconLabel = new JLabel(yourIconb);
		c.add(yourIconLabel);
		yourIconLabel.setBounds(500,10,238,60);//�{�^���̑傫���ƈʒu��ݒ肷��D(x���W�Cy���W,x�̕�,y�̕��j
		
		//�^�[�����x���̍쐬
		turnLabel = new JLabel(bturnIcon);
		c.add(turnLabel);
		turnLabel.setBounds(500,75,238,60);
		
		
		//���ʕ\�����x���̍쐬
		theLabelA = new JLabel("���ʕ\�����x��");
		theLabelB = new JLabel("���ʕ\�����x��");
		c.add(theLabelA);
		c.add(theLabelB);
		theLabelA.setBounds(545,320,45,45);
		theLabelB.setBounds(635,320,45,45);
		theLabelA.setFont(new Font("MS �S�V�b�N",Font.BOLD,25));
		theLabelB.setFont(new Font("MS �S�V�b�N",Font.BOLD,25));
		theLabelA.setText("�~2");
		theLabelB.setText("�~2");
		
        //�摜�t�����x���̍쐬
		JLabel whiteLabel = new JLabel(whiteIcon2);
        JLabel blackLabel = new JLabel(blackIcon2);
		c.add(whiteLabel);
		c.add(blackLabel);
		blackLabel.setBounds(500,320,45,45);//�{�^���̑傫���ƈʒu��ݒ肷��D(x���W�Cy���W,x�̕�,y�̕��j
        whiteLabel.setBounds(590,320,45,45);//�{�^���̑傫���ƈʒu��ݒ肷��D(x���W�Cy���W,x�̕�,y�̕��j
        //theLabel3.setForeground(Color.WHITE); //�����F�̐ݒ�DColor�̐ݒ�́C���̃y�[�W�����ĉ������@http://www.javadrive.jp/tutorial/color/
		
		 //�K�E�Z���g�p�\�ƂȂ�܂ł̃J�E���g�����Ă���郉�x���̍쐬
		actionLabel = new JLabel("�K�E�Z�J�E���g");
		c.add(actionLabel);
		actionLabel.setBounds(500,295,280,20);//�{�^���̑傫���ƈʒu��ݒ肷��D(x���W�Cy���W,x�̕�,y�̕��j
		actionLabel.setText("�K�E�Z�g�p�\�܂�10�^�[��");
		actionLabel.setFont(new Font("MS �S�V�b�N",Font.BOLD,20));
       
		//�K�E�Z�{�^���̍쐬
		actionButton=new JButton();
		c.add(actionButton);//�{�^�����R���e�i�ɒǉ�
		actionButton.setBounds(500,140,296,150);
		actionButton.addActionListener(this);//�{�^�����������Ƃ��̓��삷��悤�ɂ���
		actionButton.addMouseListener(this);//�{�^�����}�E�X�ł�������Ƃ��ɔ�������悤�ɂ���
		actionButton.addMouseMotionListener(this);//�{�^�����}�E�X�œ��������Ƃ����Ƃ��ɔ�������悤�ɂ���
		actionButton.setContentAreaFilled(false);
		
		//�w�i���x���̍쐬
		ImageIcon backgroundIcon = new ImageIcon("background.png");
		JLabel backgroundLabel = new JLabel(backgroundIcon);
		c.add(backgroundLabel);
		backgroundLabel.setBounds(0,0,800,600);
		
		
		//�T�[�o�ɐڑ�����
		Socket socket = null;
		try {
			//"localhost"�́C���������ւ̐ڑ��Dlocalhost��ڑ����IP Address�i"133.42.155.201"�`���j�ɐݒ肷��Ƒ���PC�̃T�[�o�ƒʐM�ł���
			//10000�̓|�[�g�ԍ��DIP Address�Őڑ�����PC�����߂āC�|�[�g�ԍ��ł���PC�㓮�삷��v���O��������肷��
			socket = new Socket(myipName, 10000);
		} catch (UnknownHostException e) {
			System.err.println("�z�X�g�� IP �A�h���X������ł��܂���: " + e);
		} catch (IOException e) {
			 System.err.println("�G���[���������܂���: " + e);
		}
		
		MesgRecvThread mrt = new MesgRecvThread(socket, myName);//��M�p�̃X���b�h���쐬����
		mrt.start();//�X���b�h�𓮂����iRun�������j
		
	}
		
		
	//���b�Z�[�W��M�̂��߂̃X���b�h
	public class MesgRecvThread extends Thread {
		
		Socket socket;
		String myName;
		
		public MesgRecvThread(Socket s, String n){
			socket = s;
			myName = n;
		}
		
		//�ʐM�󋵂��Ď����C��M�f�[�^�ɂ���ē��삷��
		public void run() {
			try{
				InputStreamReader sisr = new InputStreamReader(socket.getInputStream());
				BufferedReader br = new BufferedReader(sisr);
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println(myName);//�ڑ��̍ŏ��ɖ��O�𑗂�
				String myNumberStr = br.readLine();
				int myNumberInt = Integer.parseInt(myNumberStr);
				
				if(myNumberInt%2==0){
					myColor=0;
					myTurn=0;
					myIcon = whiteIcon;
					yourIcon = blackIcon;
					myturnIcon = wturnIcon;
					yourturnIcon = bturnIcon;
					actionButton.setActionCommand("skip");//�{�^���ɔz��̏���t������i�l�b�g���[�N����ăI�u�W�F�N�g�����ʂ��邽�߁j
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
					actionButton.setActionCommand("change");//�{�^���ɔz��̏���t������i�l�b�g���[�N����ăI�u�W�F�N�g�����ʂ��邽�߁j
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
					String inputLine = br.readLine();//�f�[�^����s�������ǂݍ���ł݂�
					if (inputLine != null) {//�ǂݍ��񂾂Ƃ��Ƀf�[�^���ǂݍ��܂ꂽ���ǂ������`�F�b�N����
						System.out.println(inputLine);//�f�o�b�O�i����m�F�p�j�ɃR���\�[���ɏo�͂���
						String[] inputTokens = inputLine.split(" ");	//���̓f�[�^����͂��邽�߂ɁA�X�y�[�X�Ő؂蕪����
						String cmd = inputTokens[0];//�R�}���h�̎��o���D�P�ڂ̗v�f�����o��
						
						if(cmd.equals("PLACE")){
							String theBName = inputTokens[1];
							int theBnum = Integer.parseInt(theBName);
							int y = theBnum/8;
							int x = theBnum%8;
							int theColor = Integer.parseInt(inputTokens[2]);
							if(theColor == myColor){
								//���M���N���C�A���g�ł̏���
								buttonArray[y][x].setIcon(myIcon);
								turnLabel.setIcon(yourturnIcon);
							} else {
								//���M��N���C�A���g�ł̏���
								buttonArray[y][x].setIcon(yourIcon);
								turnLabel.setIcon(myturnIcon);
							}
							myTurn=1-myTurn;
							
							if(myTurn==1){
								if(autoPass()){
									//�T�[�o�ɏ��𑗂�
									
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
							theLabelA.setText("�~"+blackCount);
							theLabelB.setText("�~"+whiteCount);
							turnCount++;
							if(turnCount<=10){
								int turnCount2 = 10-turnCount;
								actionLabel.setText("�K�E�Z�g�p�\�܂�"+turnCount2+"�^�[��");
							}
							
						}
								
						if(cmd.equals("FLIP")){
							String theBName = inputTokens[1];
							int theBnum = Integer.parseInt(theBName);
							int y = theBnum/8;
							int x = theBnum%8;
							int theColor = Integer.parseInt(inputTokens[2]);
							if(theColor == myColor){
								//���M���N���C�A���g�ł̏���
								buttonArray[y][x].setIcon(myIcon);
							} else {
								//���M��N���C�A���g�ł̏���
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
									//�T�[�o�ɏ��𑗂�
									String msg = "JUDGE";
									out.println(msg);
									out.flush();			
								}
							}
						}
						//���s����
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
						//�`���b�g
						if(cmd.equals("CHAT")){
							try{
								out = new PrintWriter(socket.getOutputStream(), true);
								out.println(myName);//�ڑ��̍ŏ��ɖ��O�𑗂�
								if (inputTokens[1] != null) {
									taMain.append(inputTokens[1]+"\n");//���b�Z�[�W�̓��e���o�͗p�e�L�X�g�ɒǉ�����
								}
								else {
									break;
								}
								
							} catch (IOException e) {
								System.err.println("�G���[���������܂���: " + e);
							}
						}
						
						//�K�E�Z�X�L�b�v
						if(cmd.equals("SKIP")){
							myTurn = 1-myTurn;
							if(myTurn==1){
								if(autoPass()){
									//�T�[�o�ɏ��𑗂�
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
						
						//�K�E�Z�`�F���W
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
									//�T�[�o�ɏ��𑗂�
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
						
						//���Z�b�g
						if(cmd.equals("RESET")) {
							for(int y=0;y<8;y++) {
								for(int x=0;x<8;x++) {
									buttonArray[y][x].setIcon(boardIcon);
								}
							}
							taMain.setText("");//taMain��Text���N���A����
							actionLabel.setText("�K�E�Z�g�p�\�܂�10�^�[��");
							theLabelA.setText("�~2");
							theLabelB.setText("�~2");
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
								actionButton.setActionCommand("change");//�{�^���ɔz��̏���t������i�l�b�g���[�N����ăI�u�W�F�N�g�����ʂ��邽�߁j
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
				System.err.println("�G���[���������܂���: " + e);
			}
		}
	}

	public static void main(String[] args) {
		MyClient net = new MyClient();
		net.setVisible(true);
	}
  	
	public void mouseClicked(MouseEvent e) {//�{�^�����N���b�N�����Ƃ��̏���
		JButton theButton = (JButton)e.getComponent();//�N���b�N�����I�u�W�F�N�g�𓾂�D�^���Ⴄ�̂ŃL���X�g����
		String theArrayIndex = theButton.getActionCommand();//�{�^���̔z��̔ԍ������o��
		Icon theIcon = theButton.getIcon();//theIcon�ɂ́C���݂̃{�^���ɐݒ肳�ꂽ�A�C�R��������
		
		//�K�E�Z�X�L�b�v�B�^�[����؂�ւ���
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
		//�K�E�Z�`�F���W�B����̋�������̋�ɂ���
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
		// �`���b�g�̃{�^���������ꂽ��(Send��������j
		if(theArrayIndex.equals("Send")){
				String msg = "CHAT"+" "+tfKeyin.getText();//���͂����e�L�X�g�𓾂�
				if(tfKeyin.getText().length()>0){//���͂������b�Z�[�W�̒������O�Ŗ�����΁C
					tfKeyin.setText("");//tfKeyin��Text���N���A����
					out.println(msg);
					out.flush();
				}
			
		}
		
		//BGM�{�^���������ꂽ�特�y��炷
		if(theArrayIndex.equals("Sound")&&stopflag==0){
            theSoundPlayer= new SoundPlayer("track_01.wav");
            theSoundPlayer.SetLoop(true);//�a�f�l�Ƃ��čĐ����J��Ԃ�
            theSoundPlayer.play();
			stopflag++;
        }
		//BGM�{�^���������ꂽ�特�y���~�߂�
		else if(theArrayIndex.equals("Sound")&&stopflag!=0){
			theSoundPlayer.stop();
			stopflag=0;
		}
		
		//���Z�b�g�{�^���������ꂽ��
		if(theArrayIndex.equals("Reset")){
			String msg = "RESET";
			out.println(msg);
			out.flush();
		}	
			
		// else �I�Z���̃{�^���������ꂽ��
		else{
			if(myTurn==1){
				System.out.println(theIcon);//�f�o�b�O�i�m�F�p�j�ɁC�N���b�N�����A�C�R���̖��O���o�͂���
				if(theIcon==boardIcon||theIcon==nextIcon) {
				
					/*if(myColor==0){//�A�C�R����whiteIcon�Ɠ����Ȃ�
						theButton.setIcon(blackIcon);//blackIcon�ɐݒ肷��
					}
					if(myColor==1){
						theButton.setIcon(whiteIcon);//whiteIcon�ɐݒ肷��
					}*/
					int temp = Integer.parseInt(theArrayIndex);
					int x = temp%8;
					int y = temp/8;
					if(judgeButton(y,x)){
						String msg = "PLACE" + " " + theArrayIndex + " " + myColor;
						out.println(msg);
						out.flush();
						repaint();//��ʂ̃I�u�W�F�N�g��`�悵����
					}
					else{
						System.out.println("�����ɂ͔z�u�͂ł��܂���");
					}
				}
			}
		}
			
		
	}
	
	public void mouseEntered(MouseEvent e) {//�}�E�X���I�u�W�F�N�g�ɓ������Ƃ��̏���
		//System.out.println("�}�E�X��������");
	}
	
	public void mouseExited(MouseEvent e) {//�}�E�X���I�u�W�F�N�g����o���Ƃ��̏���
		//System.out.println("�}�E�X�E�o");
	}
	
	public void mousePressed(MouseEvent e) {//�}�E�X�ŃI�u�W�F�N�g���������Ƃ��̏����i�N���b�N�Ƃ̈Ⴂ�ɒ��Ӂj
		//System.out.println("�}�E�X��������");
	}
	
	public void mouseReleased(MouseEvent e) {//�}�E�X�ŉ����Ă����I�u�W�F�N�g�𗣂����Ƃ��̏���
		//System.out.println("�}�E�X�������");
	}
	
	public void mouseDragged(MouseEvent e) {//�}�E�X�ŃI�u�W�F�N�g�Ƃ��h���b�O���Ă���Ƃ��̏���
		
		/*System.out.println("�}�E�X���h���b�O");
		JButton theButton = (JButton)e.getComponent();//�^���Ⴄ�̂ŃL���X�g����
		String theArrayIndex = theButton.getActionCommand();//�{�^���̔z��̔ԍ������o��
		Point theMLoc = e.getPoint();//�������R���|�[�l���g����Ƃ��鑊�΍��W
		System.out.println(theMLoc);//�f�o�b�O�i�m�F�p�j�ɁC�擾�����}�E�X�̈ʒu���R���\�[���ɏo�͂���
		Point theBtnLocation = theButton.getLocation();//�N���b�N�����{�^�������W���擾����
		theBtnLocation.x += theMLoc.x-15;//�{�^���̐^�񒆓�����Ƀ}�E�X�J�[�\��������悤�ɕ␳����
		theBtnLocation.y += theMLoc.y-15;//�{�^���̐^�񒆓�����Ƀ}�E�X�J�[�\��������悤�ɕ␳����
		theButton.setLocation(theBtnLocation);//�}�E�X�̈ʒu�ɂ��킹�ăI�u�W�F�N�g���ړ�����
		//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
		String msg = "MOVE"+" "+theArrayIndex+" "+theBtnLocation.x+" "+theBtnLocation.y;
		//�T�[�o�ɏ��𑗂�
		out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
		out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
		repaint();//�I�u�W�F�N�g�̍ĕ`����s��*/
	}

	public void mouseMoved(MouseEvent e) {//�}�E�X���I�u�W�F�N�g��ňړ������Ƃ��̏���
		/*System.out.println("�}�E�X�ړ�");
		int theMLocX = e.getX();//�}�E�X��x���W�𓾂�
		int theMLocY = e.getY();//�}�E�X��y���W�𓾂�
		System.out.println(theMLocX+","+theMLocY);//�R���\�[���ɏo�͂���*/
	}
	
		//�A�N�V�������s��ꂽ�Ƃ��̏���
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
					//�{�^���̈ʒu�������
					int msgy = y + dy;
					int msgx = x + dx;
					int theArrayIndex = msgy*8 + msgx;
				  
					//�T�[�o�ɏ��𑗂�
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
	
	//�p�X���邩�ǂ����𔻒肵�Ă����B�u���Ȃ��i�p�X�j�̂Ƃ�true��Ԃ��B
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
	
	//������邩�u���Ȃ����T�[�`
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
	
	//�}�b�v�@�\�B���ɂǂ��ɒu������ǂ��̂���\�����Ă����B
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
	
	//���s����̃_�C�A���O��\�����Ă����B
	public void showJudgeWindow(String str){
		DialogWindow dlg = new DialogWindow(this,str);
		setVisible(true);
	}
	
	//BGM�Ɋւ���N���X
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
                    long time = (long)clip.getFrameLength();//44100�Ŋ���ƍĐ����ԁi�b�j���ł�
                    System.out.println("PlaySound time="+time);
                    long endTime = System.currentTimeMillis()+time*1000/44100;
                    clip.start();
                    System.out.println("PlaySound time="+(int)(time/44100));
                    while(true){
                        if(stopFlag){//stopFlag��true�ɂȂ����I��
                            System.out.println("PlaySound stop by stopFlag");
                            clip.stop();
                            return;
                        }
                        //System.out.println("endTime="+endTime);
                        //System.out.println("currentTimeMillis="+System.currentTimeMillis());
                        if(endTime < System.currentTimeMillis()){//�Ȃ̒������߂�����I��
                            System.out.println("PlaySound stop by sound length");
                            if(loopFlag) {
                                clip.loop(1);//�������[�v�ƂȂ�
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
//�_�C�A���O�̂��߂̃N���X
//�G���N���b�N���������悤�ɂ��Ă���
class DialogWindow extends JDialog implements ActionListener{
		DialogWindow(JFrame owner,String str) {
        super(owner);//�Ăяo�����ƂƂ̐e�q�֌W�̐ݒ�D������R�����g�A�E�g����ƕʁX�̃_�C�A���O�ɂȂ�

		Container c = this.getContentPane();	//�t���[���̃y�C�����擾����
        c.setLayout(null);		//�������C�A�E�g�̐ݒ���s��Ȃ�

        JButton theButton = new JButton();//�摜��\��t���郉�x��
        ImageIcon theImage = new ImageIcon(str);//�Ȃɂ��摜�t�@�C�����_�E�����[�h���Ă���
        theButton.setIcon(theImage);//���x����ݒ�
        theButton.setBounds(0,0,600,258);//�{�^���̑傫���ƈʒu��ݒ肷��D(x���W�Cy���W,x�̕�,y�̕��j
        theButton.addActionListener(this);//�{�^�����N���b�N�����Ƃ���actionPerformed�Ŏ󂯎�邽��
        c.add(theButton);//�_�C�A���O�ɓ\��t����i�\��t���Ȃ��ƕ\������Ȃ�
        setTitle("");//�^�C�g���̐ݒ�
        setSize(600,258);//�傫���̐ݒ�
        setResizable(false);//�g��k���֎~//true�ɂ���Ɗg��k���ł���悤�ɂȂ�
        setUndecorated(true);//�^�C�g����\�����Ȃ�
        setModal(true);//������܂ŉ���G��Ȃ�����ifalse�ɂ���ƐG���j

        //�_�C�A���O�̑傫����\���ꏊ��ύX�ł���
        //�e�̃_�C�A���O�̒��S�ɕ\���������ꍇ�́C�e�̃E�B���h�E�̒��S���W�����߂āC�q�̃_�C�A���O�̑傫���̔������炷
        setLocation(owner.getBounds().x+owner.getWidth()/2-this.getWidth()/2,owner.getBounds().y+owner.getHeight()/2-this.getHeight()/2);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        this.dispose();//Dialog��p������
    }
}






