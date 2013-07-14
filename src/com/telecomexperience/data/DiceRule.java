package com.telecomexperience.data;

import android.util.Log;

public class DiceRule {

	private static final String TAG = "DiceRule";

	public static void main(String [] args){
		new DiceRule().calculationRule( generateNumber());
	}
	
	public String[] results = new String []{
											"״Ԫ���","���ú�","��ؽ�"
											,"������","���ӵǿ�","״Ԫ"
											,"����","����","�Ľ�"
											,"����","һ��","�տ�"};
	
	private static DiceRule  instance = new DiceRule();
	
	private DiceRule(){}
	
	public static DiceRule getInstance(){
		return instance;
	}
	
	public String getResult(int[] args){
		return getResult(calculationRule(args));
	}
	
	public String getResult(int position){
		if(position>0&&position<results.length){
			return results[position];
		}
		return "";
	}
	
	public int calculationRule(int[] args){
		maoPaoSort(args);
		Log.i(TAG,"����:"+args[0]+","
				+args[1]+","
				+args[2]+","
				+args[3]+","
				+args[4]+","
				+args[5]);
		if(isTheVeryBest(args)){
			System.out.println("״Ԫ���");
			return 0;
		}
		else if(isSuccessInEveryField(args)){
			System.out.println("���ú�");
			return 1;
		}
		else if(isTheLandOf(args)){
			System.out.println("��ؽ�");
			return 2;
		}
		
		else if(isSixBobBlack(args)){
			System.out.println("������");
			return 3;
		}
		
		else if(isTheKingFive(args)){
			System.out.println("���ӵǿ�");
			return 4;
		}
		else if(isTheFourRed(args)){
			System.out.println("״Ԫ");
			return 5;
		}
		
		else if(isTheHallOfFame(args)){
			System.out.println("����");
			return 6;
		}
		
		else if(isThreeRed(args)){
			System.out.println("����");
			return 7;
		}
		
		else if(isFourInto(args)){
			System.out.println("�Ľ�");
			return 8;
		}
		
		else if(isTwoRed(args)){
			System.out.println("����");
			return 9;
		}
		
		else if(isOneRed(args)){
			System.out.println("һ��");
			return 10;
		}
		
		else{
			System.out.println("�տ�");
			return 11;
		}
	}
	
	public static int[] generateNumber(){
		int length = 6;
		int [] args = new int[length];
		
		for(int i=0;i<length;i++){
			args[i] = (int)(Math.random()*6);
			System.out.println(args[i]);
		}		
		
		return args;
	}
	
	/**
	 * ״Ԫ
	 * @param args
	 * @return
	 */
	public boolean isTheVeryBest(int[] args){
		if(args==null||args.length!=6) return false;
		boolean bool = false;
		if(args[0]==0&&args[1]==0&&args[2]==3&&args[3]==3&&args[4]==3&&args[5]==3){
			bool = true;
		}
		return bool;
	}
	
	/**
	 * ���ú�
	 * @param args
	 * @return
	 */
	public boolean isSuccessInEveryField(int[] args){
		if(args==null||args.length!=6) return false;
		boolean bool = false;
		if(args[0]==3&&args[1]==3&&args[2]==3&&args[3]==3&&args[4]==3&&args[5]==3){
			bool = true;
		}
		return bool;
	}
	
	/**
	 * ��ؽ�
	 * @param args
	 * @return
	 */
	public boolean isTheLandOf(int[] args){
		if(args==null||args.length!=6) return false;
		boolean bool = false;
		if(args[0]==0&&args[1]==0&&args[2]==0&&args[3]==0&&args[4]==0&&args[5]==0){
			bool = true;
		}
		return bool;
	}
	
	/**
	 * ������
	 * @param args
	 * @return
	 */
	public boolean isSixBobBlack(int[] args){
		if(args==null||args.length!=6) return false;
		boolean bool = false;
		if(args[0]==1&&args[1]==1&&args[2]==1&&args[3]==1&&args[4]==1&&args[5]==1){
			bool = true;
		}
		return bool;
	}
	
	/**
	 * ���ӵǿ�
	 * @param args
	 * @return
	 */
	public boolean isTheKingFive(int[] args){
		if(args==null||args.length!=6) return false;
		boolean bool = false;
		if(args[1]==3&&args[2]==3&&args[3]==3&&args[4]==3&&(args[0]==3||args[5]==3)){
			bool = true;
		}
		return bool;
	}
	
	/**
	 * ״Ԫ
	 * @param args
	 * @return
	 */
	public boolean isTheFourRed(int[] args){
		if(args==null||args.length!=6) return false;
		boolean bool = false;
		int count = 0;
		for(int i=0;i<6;i++){
			if(args[i]==3){
				count++;
			}
		}
		if(count==4) bool = true;
		return bool;
	}
	
	/**
	 * ����
	 * @param args
	 * @return
	 */
	public boolean isTheHallOfFame(int[] args){
		if(args==null||args.length!=6) return false;
		boolean bool = false;
		if(args[0]==0&&args[1]==1&&args[2]==2&&args[3]==3&&args[4]==4&&args[5]==5){
			bool = true;
		}
		return bool;
	}
	
	/**
	 * ����
	 * @param args
	 * @return
	 */
	public boolean isThreeRed(int[] args){
		if(args==null||args.length!=6) return false;
		boolean bool = false;
		int count = 0;
		for(int i=0;i<6;i++){
			if(args[i]==3){
				count++;
			}
		}
		if(count==3) bool = true;
		return bool;
	}
	
	
	/**
	 * �Ľ�
	 * @param args
	 * @return
	 */
	public boolean isFourInto(int[] args){
		if(args==null||args.length!=6) return false;
		boolean bool = false;
		int count = 0;
		
		for(int j=0;j<6;j++){
			count =0;
			for(int i=0;i<6;i++){
				if(args[i]==j){
					count++;
				}
			}
			if(count==4) {
				bool = true;
				return bool;
			}
		}
		
		
		return bool;
	}
	
	/**
	 * ����
	 * @param args
	 * @return
	 */
	public boolean isTwoRed(int[] args){
		if(args==null||args.length!=6) return false;
		boolean bool = false;
		int count = 0;
		for(int i=0;i<6;i++){
			if(args[i]==3){
				count++;
			}
		}
		if(count==2) bool = true;
		return bool;
	}
	
	/**
	 * һ��
	 * @param args
	 * @return
	 */
	public boolean isOneRed(int[] args){
		if(args==null||args.length!=6) return false;
		boolean bool = false;
		int count = 0;
		for(int i=0;i<6;i++){
			if(args[i]==3){
				count++;
			}
		}
		if(count==1) bool = true;
		return bool;
	}
	
	public void maoPaoSort(int[] args){
		if(args==null) return;
		for (int i = 0; i < args.length; i++) {
			for (int j = 0; j < args.length; j++) {
				int temp;
				if (args[i] < args[j]) {
					temp = args[j];
					args[j] = args[i];
					args[i] = temp;
				}
			}
		}
		
	}
	
}
