package code;

public class GenericSearch {
	 //public static HashMap<String, Integer>IntialState;

	    public static void main(String[] args) {
	          
	        String init = "50;" +
	                "22,22,22;" +
	                "50,60,70;" +
	                "30,2;19,1;15,1;" +
	                "300,5,7,3,20;" +
	                "500,8,6,3,40;";
	    	String initialState0 = "17;" +
	                "49,30,46;" +
	                "7,57,6;" +
	                "7,1;20,2;29,2;" +
	                "350,10,9,8,28;" +
	                "408,8,12,13,34;";
	        
	        String initialState2 = "30;" +
	                "30,25,19;" +
	                "90,120,150;" +
	                "9,2;13,1;11,1;" +
	                "3195,11,12,10,34;" +
	                "691,7,8,6,15;";
	        String initialState5 = "72;" +
	                "36,13,35;" +
	                "75,96,62;" +
	                "20,2;5,2;33,2;" +
	                "30013,7,6,3,36;" +
	                "40050,5,10,14,44;";

	    	String initialState1 = "50;" +
	                "12,12,12;" +
	                "50,60,70;" +
	                "30,2;19,2;15,2;" +
	                "300,5,7,3,20;" +
	                "500,8,6,3,40;";
	    	String initialState10= "32;"+
	    			"20,16,11;" +
	    			"76,14,14;" +
	    			"9,1;9,2;9,1;" +
	    			"358,14,25,23,39;" +
	    			"5024,20,17,17,38;";
	    	
	    	String initialState9 = "50;" +
	    			"20,16,11;" +
	    			"76,14,14;" +
	    			"7,1;7,1;7,1;" +
	    			"359,14,25,23,39;" +
	    			"524,18,17,17,38;";
	    	
	    	
	        String str="AS2";

	        System.out.println("--------------------------------------------------------------------");
	        System.out.println("Start of Solve");
	        System.out.println("--------------------------------------------------------------------");
	        LLAPSearch.solve(initialState5,str,true);


	    }
}