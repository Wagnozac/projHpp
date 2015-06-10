package fr.tse.fi2.hpp.labs.utils;

import java.util.Arrays;
import java.util.Random;


public class MergeSort  {

	
	
	public MergeSort() {
	
		// TODO Auto-generated constructor stub
		
	}

	

	
	
	public static int[] mergeIt(int[] JeanJacques){
		int a =0;
		
		if( JeanJacques.length%2==1){
			a=1;
		}
		
		

		int[] part1 = new int[JeanJacques.length/2+a];
		int[] part2 = new int[JeanJacques.length/2];
	
		
		
		if (JeanJacques.length==1)
		{
			return JeanJacques;
		}
		
		
		if ( JeanJacques.length==2)
		{
			
			if (JeanJacques[0]<JeanJacques[1])
				return JeanJacques;
			else
			{
				int[] fin = new int[2];
				fin[0]=(JeanJacques[1]);
				fin[1]=(JeanJacques[0]);
				return fin;
			}
		}
			
			
		if (JeanJacques.length<20)
		{
			
	        for(int i=1;i<JeanJacques.length;i++)
            {
            int memory=JeanJacques[i];
            int compt=i-1;
            boolean marqueur;
            do
                {
                marqueur=false;
                if (JeanJacques[compt]>memory)
                    {
                	JeanJacques[compt+1]=JeanJacques[compt];
                    compt--;
                    marqueur=true;
                    }
                if (compt<0) marqueur=false;
                }
            while(marqueur);
            JeanJacques[compt+1]=memory;
            }
			
	        return JeanJacques;		
			
		}
			
			
			
		
	
		for(int i = 0; i< JeanJacques.length/2+a; i++){
			
			part1[i]=(JeanJacques[i]);
			
		}
		
		for(int j = JeanJacques.length/2+a; j< JeanJacques.length; j++){
			
			part2[j-(JeanJacques.length/2)+a]=(JeanJacques[j]);
			
		}
		
		int[] partR1;
		partR1=mergeIt(part1);
		int[] partR2;
		partR2=mergeIt(part2);
		
		System.arraycopy(partR2, 0, partR1, partR1.length, partR2.length);
		Arrays.sort(partR1);
		return partR1;
		
		
		
	}
	
	public static int[] tabVal(int taille){
		
		int[] tab = new int[taille];
		Random rdm = new Random();
		
		for (int i = 0; i < taille; i++){
			
			int randomNum = rdm.nextInt(i+1);
			tab[i]=(randomNum);
	
		}
		
		
		return tab;
		
		
		
		
	}
	
	public static void testBen(int[] tab){
		
		mergeIt(tab);
		
	}
	

}
