package code;

import java.awt.Taskbar.State;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.util.*;

public class LLAPSearch extends GenericSearch {
    public Node node;
    public boolean Delivery;
    public HashMap<String,Integer> intialState;
    public static int heuristicFlag =0;


    //public static Queue<code.Node> Queue = new LinkedList<>();
    Queue<Node> Expansion_Sequence = new LinkedList<>();
    
    public static HashMap<String, Integer>IntialState;
    



    public static String solve( String IntialStateProblem , String strategy, Boolean visualize) {
    	
    	 String[] parts = IntialStateProblem.split("[,;]");
        String[] sectionNames = {
                "initialProsperity",
                "initialFood", "initialMaterials", "initialEnergy",
                "unitPriceFood", "unitPriceMaterials", "unitPriceEnergy",
                "amountRequestFood", "delayRequestFood",
                "amountRequestMaterials", "delayRequestMaterials",
                "amountRequestEnergy", "delayRequestEnergy",
                "priceBUILD1", "foodUseBUILD1",
                "materialsUseBUILD1", "energyUseBUILD1", "prosperityBUILD1",
                "priceBUILD2", "foodUseBUILD2",
                "materialsUseBUILD2", "energyUseBUILD2", "prosperityBUILD2"
        };

        if (parts.length != sectionNames.length) {
            System.out.println("Mismatched number of parts and section names.");
            return "Incorrect input";
        }


        IntialState = new HashMap<>(); ;
//        for (Map.Entry<String, Integer> entry : IntialState.entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }
//        

        for (int i = 0; i < parts.length; i++) {

            String variableName = sectionNames[i];
            int value = Integer.parseInt(parts[i]);
            IntialState.put(variableName, value);
        }
        //System.out.println(IntialState);

        if(strategy.equals("GR1") || strategy.equals("AS1") ) {
        	heuristicFlag=1;
        }
        

        if(strategy.equals("GR2") || strategy.equals("AS2") ) {
        	heuristicFlag=2;
        }
        
        
        
        
        int FoodTotal1=IntialState.get("unitPriceFood")*IntialState.get("foodUseBUILD1");
        int MaterialTotal1=IntialState.get("unitPriceMaterials")*IntialState.get("materialsUseBUILD1");
        int EnergyTotal1=IntialState.get("unitPriceEnergy")*IntialState.get("energyUseBUILD1");
        int BuildTotal1=IntialState.get("priceBUILD1");
        int Total1=FoodTotal1+MaterialTotal1+EnergyTotal1+BuildTotal1;
        
        int FoodTotal2=IntialState.get("unitPriceFood")*IntialState.get("foodUseBUILD2");
        int MaterialTotal2=IntialState.get("unitPriceMaterials")*IntialState.get("materialsUseBUILD2");
        int EnergyTotal2=IntialState.get("unitPriceEnergy")*IntialState.get("energyUseBUILD2");
        int BuildTotal2=IntialState.get("priceBUILD2");
        int Total2=FoodTotal2+MaterialTotal2+EnergyTotal2+BuildTotal2;
        
        
        


        HashMap<String,Integer> state = new HashMap<>();
        state.put("prosperity", IntialState.get("initialProsperity"));
        state.put("food", IntialState.get("initialFood"));
        state.put("materials", IntialState.get("initialMaterials"));
        state.put("energy", IntialState.get("initialEnergy"));
        state.put("money_spent",0);
        
        int heuristic=0;
        
        if(heuristicFlag==1) {
         	 float prosperity_distance = 100 - state.get("prosperity");
              float maxPros = Integer.max(IntialState.get("prosperityBUILD1"), IntialState.get("prosperityBUILD2"));
              float temp = (float)(prosperity_distance/maxPros);  
              heuristic = Math.round(temp*(Integer.min(Total1, Total2)));
              if(heuristic<0) {
           	   heuristic = 0;
              }
         }
        else if(heuristicFlag==2) {
         	 float prosperity_distance = 100 - state.get("prosperity");
         	 
         	 float AverageFood = Math.max(state.get("food")-IntialState.get("foodUseBUILD2"),Math.max(state.get("food")-IntialState.get("foodUseBUILD1"), 0));
         	 float AverageMaterials = Math.max(state.get("materials")-IntialState.get("materialsUseBUILD1"),Math.max(state.get("materials")-IntialState.get("materialsUseBUILD2"), 0));
         	 float AverageEnergy = Math.max(state.get("energy")-IntialState.get("energyUseBUILD1"),Math.max(state.get("energy")-IntialState.get("energyUseBUILD2"), 0));

         	 float temp = (prosperity_distance)/(Math.max(AverageFood, Math.max(AverageEnergy, Math.max(AverageMaterials, 1))));
         	 
         	  heuristic = (int) (temp * (float) (Math.min(Total1, Total2))) ;
             if(heuristic<0) {
             	   heuristic = 0;
                }
         }
        

        Node root = new Node(state,null,0,0,heuristic,false,0,"","");
        
        root.FindOperators();

        switch (strategy) {
            case "BF":
            	
                String solBF = BreadthFirstSearch(root,visualize);
                System.out.println(solBF);
                return solBF;
                
            case "DF":
            	 String solDF = DebthFirstSearch(root,visualize);
            	 System.out.println(solDF);
            	 return solDF;
               
            case "UC":
            	String solUC = UniformCostSearch(root,visualize);
            	System.out.println(solUC);
            	return solUC;
            case "ID":
            	String solID = IterativeDeepening(root,visualize);     
            	System.out.println(solID);

            	return solID;
            case "GR1":
            	heuristicFlag=1;
            	String solGR1 = GreedySearch(root,visualize);
            	System.out.println(solGR1);
            	return solGR1;
            	
            case "GR2":
            	heuristicFlag=2;
            	String solGR2 = GreedySearch(root,visualize);
            	System.out.println(solGR2);
            	return solGR2;	
            	
            case "AS1":
            	heuristicFlag=1;
            	String solAS1 = AstarSearch(root,visualize);
            	System.out.println(solAS1);
            	return solAS1;
            	
            case "AS2":
            	heuristicFlag=2;
            	String solAS2 = AstarSearch(root,visualize);
            	System.out.println(solAS2);
	
//          	  	MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
//          	  	long usedHeapMemory = heapMemoryUsage.getUsed();
//          	  	long maxHeapMemory = heapMemoryUsage.getMax();
//          	  	System.out.println("Used heap memory: " + usedHeapMemory + " bytes");
//          	  	System.out.println("Max heap memory: " + maxHeapMemory + " bytes");
//          	  	Runtime runtime = Runtime.getRuntime();
//          	  	long usedMemory = runtime.totalMemory() - runtime.freeMemory();
//          	  	System.out.println("RAM usage: " + usedMemory + " bytes");

              
            	return solAS2;
            	
            	
            default:
                return "No Strategy";
        }



    }

    public static String getPath(Node EndNode) {
    	String FinalPath="";
    	Node CurrNode=EndNode;
    	while(!CurrNode.ParentAction.equals("")) {
    		if(FinalPath=="") {
    			FinalPath=CurrNode.ParentAction+FinalPath;
    		}else {
    			FinalPath=CurrNode.ParentAction+","+FinalPath;
    		}
    		CurrNode=CurrNode.Parent;

    		
    	
    	}
    	return FinalPath;
    	
    	
    	
    	
    }

    public static String  DebthFirstSearch(Node root,Boolean visualize){
    	int FoodTotal1=LLAPSearch.IntialState.get("unitPriceFood")*LLAPSearch.IntialState.get("foodUseBUILD1");
        int MaterialTotal1=LLAPSearch.IntialState.get("unitPriceMaterials")*LLAPSearch.IntialState.get("materialsUseBUILD1");
        int EnergyTotal1=LLAPSearch.IntialState.get("unitPriceEnergy")*LLAPSearch.IntialState.get("energyUseBUILD1");
        int Build1Price=LLAPSearch.IntialState.get("priceBUILD1");
        int Build1Total=FoodTotal1+MaterialTotal1+EnergyTotal1+Build1Price;
        

        int FoodTotal2=LLAPSearch.IntialState.get("unitPriceFood")*LLAPSearch.IntialState.get("foodUseBUILD2");
        int MaterialTotal2=LLAPSearch.IntialState.get("unitPriceMaterials")*LLAPSearch.IntialState.get("materialsUseBUILD2");
        int EnergyTotal2=LLAPSearch.IntialState.get("unitPriceEnergy")*LLAPSearch.IntialState.get("energyUseBUILD2");
        int Build2Price=LLAPSearch.IntialState.get("priceBUILD2");
        int Build2Total=FoodTotal2+MaterialTotal2+EnergyTotal2+Build2Price;

        DFSQueue<Node> Queue = new DFSQueue<>();
        ArrayList<Node> Ancestors = new ArrayList<Node>();
        Queue.add(root);
        int monetaryCost = 0;
        int nodesExpanded = 0;
        while(!Queue.isEmpty()){
        	if(visualize) {
          		 System.out.println("----------------------------------------------------------------------");
                   System.out.println("Total Price for build1:"+Build1Total +" Total Energy for build1:"+LLAPSearch.IntialState.get("energyUseBUILD1")+" Total Food for build1: "+LLAPSearch.IntialState.get("foodUseBUILD1")+ " Total Materials for build1: "+LLAPSearch.IntialState.get("materialsUseBUILD1")+" Prosperity after build1:"+IntialState.get("prosperityBUILD1"));
                   System.out.println("Total Price for build2:"+Build2Total +" Total Energy for build2:"+LLAPSearch.IntialState.get("energyUseBUILD2")+" Total Food for build2 : "+LLAPSearch.IntialState.get("foodUseBUILD2")+ " Total Materials for build2: "+LLAPSearch.IntialState.get("materialsUseBUILD2")+" Prosperity after build2:"+IntialState.get("prosperityBUILD2"));
                   System.out.println("Amount per Energy order:"+LLAPSearch.IntialState.get("amountRequestEnergy"));
                   System.out.println("Amount per Food order:"+LLAPSearch.IntialState.get("amountRequestFood"));
                   System.out.println("Amount per Materials order:"+LLAPSearch.IntialState.get("amountRequestMaterials"));
                   System.out.println("------------CurrentNode---------------");
                   System.out.println("Parent Action :" + Queue.peek().ParentAction);
                   System.out.println(Queue.peek().toString());
                   System.out.println("----------------------------------------------------------------------");
          		
          	}
  
            nodesExpanded++;

            if( Queue.peek().State.get("prosperity") >= 100 ){
            	String FinalPath=getPath(Queue.peek());
                monetaryCost = Queue.peek().State.get("money_spent");
                System.out.println("depth:" + Queue.peek().depth);
                return FinalPath+";"+monetaryCost+";"+nodesExpanded;

            }

            Node currentNode = Queue.peek();
            for(int i =0 ; i<currentNode.operators.size();i++){
                if(currentNode.operators.get(i)=="RequestFood"){
                    Node A=RequestFood(currentNode);
                    boolean Repeated=checkAncestors(A,Ancestors);
                    if(!Repeated) {
                    	 Queue.add(A);
                         Ancestors.add(A);
                    }
                       


                } else if (currentNode.operators.get(i)=="RequestMaterials") {
                    Node B =RequestMaterials(currentNode);
                    boolean Repeated=checkAncestors(B,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(B);
                     Ancestors.add(B);
                   }


                } else if (currentNode.operators.get(i)=="RequestEnergy") {
                    Node C =RequestEnergy(currentNode);
                    boolean Repeated=checkAncestors(C,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(C);
                     Ancestors.add(C);
                   }

                } else if (currentNode.operators.get(i)=="BUILD1") {
                    Node D =BUILD1(currentNode);
                    boolean Repeated=checkAncestors(D,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(D);
                     Ancestors.add(D);
                   }

                }else if (currentNode.operators.get(i)=="BUILD2") {
                    Node E =BUILD2(currentNode);
                    boolean Repeated=checkAncestors(E,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(E);
                     Ancestors.add(E);
                   }

                }else if (currentNode.operators.get(i)=="WAIT") {
                    Node F =WAIT(currentNode);
                    boolean Repeated=checkAncestors(F,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(F);
                        Ancestors.add(F);
                   }

                }
            }
           
            Queue.remove(currentNode);
        }

        return "NOSOLUTION";

        

    };

    public static String BreadthFirstSearch(Node root,Boolean visualize){
    	int FoodTotal1=LLAPSearch.IntialState.get("unitPriceFood")*LLAPSearch.IntialState.get("foodUseBUILD1");
        int MaterialTotal1=LLAPSearch.IntialState.get("unitPriceMaterials")*LLAPSearch.IntialState.get("materialsUseBUILD1");
        int EnergyTotal1=LLAPSearch.IntialState.get("unitPriceEnergy")*LLAPSearch.IntialState.get("energyUseBUILD1");
        int Build1Price=LLAPSearch.IntialState.get("priceBUILD1");
        int Build1Total=FoodTotal1+MaterialTotal1+EnergyTotal1+Build1Price;
        

        int FoodTotal2=LLAPSearch.IntialState.get("unitPriceFood")*LLAPSearch.IntialState.get("foodUseBUILD2");
        int MaterialTotal2=LLAPSearch.IntialState.get("unitPriceMaterials")*LLAPSearch.IntialState.get("materialsUseBUILD2");
        int EnergyTotal2=LLAPSearch.IntialState.get("unitPriceEnergy")*LLAPSearch.IntialState.get("energyUseBUILD2");
        int Build2Price=LLAPSearch.IntialState.get("priceBUILD2");
        int Build2Total=FoodTotal2+MaterialTotal2+EnergyTotal2+Build2Price;

        Queue<Node> Queue = new LinkedList<>();
        ArrayList<Node> Ancestors = new ArrayList<Node>();
        Queue.add(root);
        int monetaryCost = 0;
        int nodesExpanded = 0;

        while(!Queue.isEmpty()){
        	if(visualize) {
        		 System.out.println("----------------------------------------------------------------------");
                 System.out.println("Total Price for build1:"+Build1Total +" Total Energy for build1:"+LLAPSearch.IntialState.get("energyUseBUILD1")+" Total Food for build1: "+LLAPSearch.IntialState.get("foodUseBUILD1")+ " Total Materials for build1: "+LLAPSearch.IntialState.get("materialsUseBUILD1")+" Prosperity after build1:"+IntialState.get("prosperityBUILD1"));
                 System.out.println("Total Price for build2:"+Build2Total +" Total Energy for build2:"+LLAPSearch.IntialState.get("energyUseBUILD2")+" Total Food for build2 : "+LLAPSearch.IntialState.get("foodUseBUILD2")+ " Total Materials for build2: "+LLAPSearch.IntialState.get("materialsUseBUILD2")+" Prosperity after build2:"+IntialState.get("prosperityBUILD2"));
                 System.out.println("Amount per Energy order:"+LLAPSearch.IntialState.get("amountRequestEnergy"));
                 System.out.println("Amount per Food order:"+LLAPSearch.IntialState.get("amountRequestFood"));
                 System.out.println("Amount per Materials order:"+LLAPSearch.IntialState.get("amountRequestMaterials"));
                 System.out.println("------------CurrentNode---------------");
                 System.out.println("Parent Action :" + Queue.peek().ParentAction);
                 System.out.println(Queue.peek().toString());
                 System.out.println("----------------------------------------------------------------------");
        		
        	}
           
            nodesExpanded++;

            if( Queue.peek().State.get("prosperity") >= 100 ){
                String FinalPath=getPath(Queue.peek());
                monetaryCost = Queue.peek().State.get("money_spent");
                return FinalPath+";"+monetaryCost+";"+nodesExpanded;
            }

            Node currentNode = Queue.peek();
            for(int i =0 ; i<currentNode.operators.size();i++){
                if(currentNode.operators.get(i)=="RequestFood"){
                    Node A=RequestFood(currentNode);
                    boolean Repeated=checkAncestors(A,Ancestors);
                    if(!Repeated) {
                    	 Queue.add(A);
                         Ancestors.add(A);
                    }
                       


                } else if (currentNode.operators.get(i)=="RequestMaterials") {
                    Node B =RequestMaterials(currentNode);
                    boolean Repeated=checkAncestors(B,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(B);
                     Ancestors.add(B);
                   }


                } else if (currentNode.operators.get(i)=="RequestEnergy") {
                    Node C =RequestEnergy(currentNode);
                    boolean Repeated=checkAncestors(C,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(C);
                     Ancestors.add(C);
                   }

                } else if (currentNode.operators.get(i)=="BUILD1") {
                    Node D =BUILD1(currentNode);
                    boolean Repeated=checkAncestors(D,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(D);
                    Ancestors.add(D);
                   }

                }else if (currentNode.operators.get(i)=="BUILD2") {
                    Node E =BUILD2(currentNode);
                    boolean Repeated=checkAncestors(E,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(E);
                     Ancestors.add(E);
                   }

                }else if (currentNode.operators.get(i)=="WAIT") {
                    Node F =WAIT(currentNode);
                    boolean Repeated=checkAncestors(F,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(F);
                    Ancestors.add(F);
                   }

                }
            }
           
            Queue.remove(currentNode);  
        }

            return "NOSOLUTION";
        
       

    };

    public static String  UniformCostSearch(Node root,Boolean visualize){
    	int FoodTotal1=LLAPSearch.IntialState.get("unitPriceFood")*LLAPSearch.IntialState.get("foodUseBUILD1");
        int MaterialTotal1=LLAPSearch.IntialState.get("unitPriceMaterials")*LLAPSearch.IntialState.get("materialsUseBUILD1");
        int EnergyTotal1=LLAPSearch.IntialState.get("unitPriceEnergy")*LLAPSearch.IntialState.get("energyUseBUILD1");
        int Build1Price=LLAPSearch.IntialState.get("priceBUILD1");
        int Build1Total=FoodTotal1+MaterialTotal1+EnergyTotal1+Build1Price;
        

        int FoodTotal2=LLAPSearch.IntialState.get("unitPriceFood")*LLAPSearch.IntialState.get("foodUseBUILD2");
        int MaterialTotal2=LLAPSearch.IntialState.get("unitPriceMaterials")*LLAPSearch.IntialState.get("materialsUseBUILD2");
        int EnergyTotal2=LLAPSearch.IntialState.get("unitPriceEnergy")*LLAPSearch.IntialState.get("energyUseBUILD2");
        int Build2Price=LLAPSearch.IntialState.get("priceBUILD2");
        int Build2Total=FoodTotal2+MaterialTotal2+EnergyTotal2+Build2Price;

       PriorityQueue<Node> Queue = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.State.get("money_spent") - o2.State.get("money_spent");
            }
        });
        ArrayList<Node> Ancestors = new ArrayList<Node>();
        Queue.add(root);
        int monetaryCost = 0;
        int nodesExpanded = 0;
        ArrayList<String> plan = new ArrayList<String>();
        
        Ancestors.add(root);

        while(!Queue.isEmpty()){
        	if(visualize) {
       		 System.out.println("----------------------------------------------------------------------");
                System.out.println("Total Price for build1:"+Build1Total +" Total Energy for build1:"+LLAPSearch.IntialState.get("energyUseBUILD1")+" Total Food for build1: "+LLAPSearch.IntialState.get("foodUseBUILD1")+ " Total Materials for build1: "+LLAPSearch.IntialState.get("materialsUseBUILD1")+" Prosperity after build1:"+IntialState.get("prosperityBUILD1"));
                System.out.println("Total Price for build2:"+Build2Total +" Total Energy for build2:"+LLAPSearch.IntialState.get("energyUseBUILD2")+" Total Food for build2 : "+LLAPSearch.IntialState.get("foodUseBUILD2")+ " Total Materials for build2: "+LLAPSearch.IntialState.get("materialsUseBUILD2")+" Prosperity after build2:"+IntialState.get("prosperityBUILD2"));
                System.out.println("Amount per Energy order:"+LLAPSearch.IntialState.get("amountRequestEnergy"));
                System.out.println("Amount per Food order:"+LLAPSearch.IntialState.get("amountRequestFood"));
                System.out.println("Amount per Materials order:"+LLAPSearch.IntialState.get("amountRequestMaterials"));
                System.out.println("------------CurrentNode---------------");
                System.out.println("Parent Action :" + Queue.peek().ParentAction);
                System.out.println(Queue.peek().toString());
                System.out.println("----------------------------------------------------------------------");
       		
       	}
            nodesExpanded++;

            plan.add(Queue.peek().ParentAction);
            if( Queue.peek().State.get("prosperity") >= 100 ){
            	String FinalPath=getPath(Queue.peek());
                monetaryCost = Queue.peek().State.get("money_spent");
                return FinalPath+";"+monetaryCost+";"+nodesExpanded;
            }

            Node currentNode = Queue.peek();
            for(int i =0 ; i<currentNode.operators.size();i++){
                if(currentNode.operators.get(i)=="RequestFood"){
                    Node A=RequestFood(currentNode);
                    boolean Repeated=checkAncestors(A,Ancestors);
                    if(!Repeated) {
                    	 Queue.add(A);
                         Ancestors.add(A);
                    }
                       


                } else if (currentNode.operators.get(i)=="RequestMaterials") {
                    Node B =RequestMaterials(currentNode);
                    boolean Repeated=checkAncestors(B,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(B);
                     Ancestors.add(B);
                   }


                } else if (currentNode.operators.get(i)=="RequestEnergy") {
                    Node C =RequestEnergy(currentNode);
                    boolean Repeated=checkAncestors(C,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(C);
                     Ancestors.add(C);
                   }

                } else if (currentNode.operators.get(i)=="BUILD1") {
                    Node D =BUILD1(currentNode);
                    boolean Repeated=checkAncestors(D,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(D);
                     Ancestors.add(D);
                   }

                }else if (currentNode.operators.get(i)=="BUILD2") {
                    Node E =BUILD2(currentNode);
                    boolean Repeated=checkAncestors(E,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(E);
                     Ancestors.add(E);
                   }

                }else if (currentNode.operators.get(i)=="WAIT") {
                    Node F =WAIT(currentNode);
                    boolean Repeated=checkAncestors(F,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(F);
                        Ancestors.add(F);
                   }

                }
            }
           
            Queue.remove(currentNode);
        }

        return "NOSOLUTION";



    };

    public static String  IterativeDeepening(Node root,Boolean visualize){
    	int FoodTotal1=LLAPSearch.IntialState.get("unitPriceFood")*LLAPSearch.IntialState.get("foodUseBUILD1");
        int MaterialTotal1=LLAPSearch.IntialState.get("unitPriceMaterials")*LLAPSearch.IntialState.get("materialsUseBUILD1");
        int EnergyTotal1=LLAPSearch.IntialState.get("unitPriceEnergy")*LLAPSearch.IntialState.get("energyUseBUILD1");
        int Build1Price=LLAPSearch.IntialState.get("priceBUILD1");
        int Build1Total=FoodTotal1+MaterialTotal1+EnergyTotal1+Build1Price;
        

        int FoodTotal2=LLAPSearch.IntialState.get("unitPriceFood")*LLAPSearch.IntialState.get("foodUseBUILD2");
        int MaterialTotal2=LLAPSearch.IntialState.get("unitPriceMaterials")*LLAPSearch.IntialState.get("materialsUseBUILD2");
        int EnergyTotal2=LLAPSearch.IntialState.get("unitPriceEnergy")*LLAPSearch.IntialState.get("energyUseBUILD2");
        int Build2Price=LLAPSearch.IntialState.get("priceBUILD2");
        int Build2Total=FoodTotal2+MaterialTotal2+EnergyTotal2+Build2Price;

//        DFSQueue<Node> Queue = new DFSQueue<>();
//        ArrayList<Node> Ancestors = new ArrayList<Node>();
//        Queue.add(root);
        int monetaryCost = 0;
        int nodesExpanded = 0;
    	DFSQueue<Node> Queue = new DFSQueue<>();
        ArrayList<Node> Ancestors = new ArrayList<Node>();
        Queue.add(root);
        
        for (int depthLimit = 0; depthLimit < Integer.MAX_VALUE; depthLimit++) {
            
        	while(!Queue.isEmpty()){
        	Node currentNode = Queue.peek();

        	if(visualize) {
          		 System.out.println("----------------------------------------------------------------------");
                   System.out.println("Total Price for build1:"+Build1Total +" Total Energy for build1:"+LLAPSearch.IntialState.get("energyUseBUILD1")+" Total Food for build1: "+LLAPSearch.IntialState.get("foodUseBUILD1")+ " Total Materials for build1: "+LLAPSearch.IntialState.get("materialsUseBUILD1")+" Prosperity after build1:"+IntialState.get("prosperityBUILD1"));
                   System.out.println("Total Price for build2:"+Build2Total +" Total Energy for build2:"+LLAPSearch.IntialState.get("energyUseBUILD2")+" Total Food for build2 : "+LLAPSearch.IntialState.get("foodUseBUILD2")+ " Total Materials for build2: "+LLAPSearch.IntialState.get("materialsUseBUILD2")+" Prosperity after build2:"+IntialState.get("prosperityBUILD2"));
                   System.out.println("Amount per Energy order:"+LLAPSearch.IntialState.get("amountRequestEnergy"));
                   System.out.println("Amount per Food order:"+LLAPSearch.IntialState.get("amountRequestFood"));
                   System.out.println("Amount per Materials order:"+LLAPSearch.IntialState.get("amountRequestMaterials"));
                   System.out.println("------------CurrentNode---------------");
                   System.out.println("Parent Action :" + Queue.peek().ParentAction);
                   System.out.println(Queue.peek().toString());
                   System.out.println("----------------------------------------------------------------------");
          		
          	}
  
            nodesExpanded++;

            if( Queue.peek().State.get("prosperity") >= 100 ){
            	String FinalPath=getPath(Queue.peek());
                monetaryCost = Queue.peek().State.get("money_spent");
                return FinalPath+";"+monetaryCost+";"+nodesExpanded ;

            }

            for(int i =0 ; i<currentNode.operators.size();i++){
                if(currentNode.operators.get(i)=="RequestFood"){
                    Node A=RequestFood(currentNode);
                    boolean Repeated=checkAncestors(A,Ancestors);
                    if(!Repeated && A.depth<=depthLimit) {
                    	 Queue.add(A);
                         Ancestors.add(A);
                    }
                       


                } else if (currentNode.operators.get(i)=="RequestMaterials") {
                    Node B =RequestMaterials(currentNode);
                    boolean Repeated=checkAncestors(B,Ancestors);
                    if(!Repeated&& B.depth<=depthLimit) {
                   	 Queue.add(B);
                     Ancestors.add(B);
                   }


                } else if (currentNode.operators.get(i)=="RequestEnergy") {
                    Node C =RequestEnergy(currentNode);
                    boolean Repeated=checkAncestors(C,Ancestors);
                    if(!Repeated && C.depth<=depthLimit) {
                   	 Queue.add(C);
                     Ancestors.add(C);
                   }

                } else if (currentNode.operators.get(i)=="BUILD1") {
                    Node D =BUILD1(currentNode);
                    boolean Repeated=checkAncestors(D,Ancestors);
                    if(!Repeated && D.depth<=depthLimit) {
                   	 Queue.add(D);
                     Ancestors.add(D);
                   }

                }else if (currentNode.operators.get(i)=="BUILD2") {
                    Node E =BUILD2(currentNode);
                    boolean Repeated=checkAncestors(E,Ancestors);
                    if(!Repeated && E.depth<=depthLimit) {
                   	 Queue.add(E);
                     Ancestors.add(E);
                   }

                }else if (currentNode.operators.get(i)=="WAIT") {
                    Node F =WAIT(currentNode);
                    boolean Repeated=checkAncestors(F,Ancestors);
                    if(!Repeated && F.depth<=depthLimit) {
                   	 Queue.add(F);
                        Ancestors.add(F);
                   }

                }
            }
           
            Queue.remove(currentNode);
       
            
        	}
        	Queue.clear();
        	Ancestors.clear();
        	Queue.add(root);
        	Ancestors.add(root);
        }
       

        return "NOSOLUTION";

        

    };
    
    public static String GreedySearch(Node root,Boolean visualize)    {
    	int FoodTotal1=LLAPSearch.IntialState.get("unitPriceFood")*LLAPSearch.IntialState.get("foodUseBUILD1");
        int MaterialTotal1=LLAPSearch.IntialState.get("unitPriceMaterials")*LLAPSearch.IntialState.get("materialsUseBUILD1");
        int EnergyTotal1=LLAPSearch.IntialState.get("unitPriceEnergy")*LLAPSearch.IntialState.get("energyUseBUILD1");
        int Build1Price=LLAPSearch.IntialState.get("priceBUILD1");
        int Build1Total=FoodTotal1+MaterialTotal1+EnergyTotal1+Build1Price;
        

        int FoodTotal2=LLAPSearch.IntialState.get("unitPriceFood")*LLAPSearch.IntialState.get("foodUseBUILD2");
        int MaterialTotal2=LLAPSearch.IntialState.get("unitPriceMaterials")*LLAPSearch.IntialState.get("materialsUseBUILD2");
        int EnergyTotal2=LLAPSearch.IntialState.get("unitPriceEnergy")*LLAPSearch.IntialState.get("energyUseBUILD2");
        int Build2Price=LLAPSearch.IntialState.get("priceBUILD2");
        int Build2Total=FoodTotal2+MaterialTotal2+EnergyTotal2+Build2Price;

       PriorityQueue<Node> Queue = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.heuristic - o2.heuristic;
            }
        });
        ArrayList<Node> Ancestors = new ArrayList<Node>();
        Queue.add(root);
        int monetaryCost = 0;
        int nodesExpanded = 0;
        
        Ancestors.add(root);

        while(!Queue.isEmpty()){
        	if(visualize) {
       		 System.out.println("----------------------------------------------------------------------");
                System.out.println("Total Price for build1:"+Build1Total +" Total Energy for build1:"+LLAPSearch.IntialState.get("energyUseBUILD1")+" Total Food for build1: "+LLAPSearch.IntialState.get("foodUseBUILD1")+ " Total Materials for build1: "+LLAPSearch.IntialState.get("materialsUseBUILD1")+" Prosperity after build1:"+IntialState.get("prosperityBUILD1"));
                System.out.println("Total Price for build2:"+Build2Total +" Total Energy for build2:"+LLAPSearch.IntialState.get("energyUseBUILD2")+" Total Food for build2 : "+LLAPSearch.IntialState.get("foodUseBUILD2")+ " Total Materials for build2: "+LLAPSearch.IntialState.get("materialsUseBUILD2")+" Prosperity after build2:"+IntialState.get("prosperityBUILD2"));
                System.out.println("Amount per Energy order:"+LLAPSearch.IntialState.get("amountRequestEnergy"));
                System.out.println("Amount per Food order:"+LLAPSearch.IntialState.get("amountRequestFood"));
                System.out.println("Amount per Materials order:"+LLAPSearch.IntialState.get("amountRequestMaterials"));
                System.out.println("------------CurrentNode---------------");
                System.out.println("Parent Action :" + Queue.peek().ParentAction);
                System.out.println(Queue.peek().toString());
                System.out.println("----------------------------------------------------------------------");
       		
       	}
            nodesExpanded++;

            if( Queue.peek().State.get("prosperity") >= 100 ){
            	String FinalPath=getPath(Queue.peek());
                monetaryCost = Queue.peek().State.get("money_spent");
                return FinalPath+";"+monetaryCost+";"+nodesExpanded;
            }

            Node currentNode = Queue.peek();
            for(int i =0 ; i<currentNode.operators.size();i++){
                if(currentNode.operators.get(i)=="RequestFood"){
                    Node A=RequestFood(currentNode);
                    boolean Repeated=checkAncestors(A,Ancestors);
                    if(!Repeated) {
                    	 Queue.add(A);
                         Ancestors.add(A);
                    }
                       


                } else if (currentNode.operators.get(i)=="RequestMaterials") {
                    Node B =RequestMaterials(currentNode);
                    boolean Repeated=checkAncestors(B,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(B);
                     Ancestors.add(B);
                   }


                } else if (currentNode.operators.get(i)=="RequestEnergy") {
                    Node C =RequestEnergy(currentNode);
                    boolean Repeated=checkAncestors(C,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(C);
                     Ancestors.add(C);
                   }

                } else if (currentNode.operators.get(i)=="BUILD1") {
                    Node D =BUILD1(currentNode);
                    boolean Repeated=checkAncestors(D,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(D);
                     Ancestors.add(D);
                   }

                }else if (currentNode.operators.get(i)=="BUILD2") {
                    Node E =BUILD2(currentNode);
                    boolean Repeated=checkAncestors(E,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(E);
                     Ancestors.add(E);
                   }

                }else if (currentNode.operators.get(i)=="WAIT") {
                    Node F =WAIT(currentNode);
                    boolean Repeated=checkAncestors(F,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(F);
                        Ancestors.add(F);
                   }

                }
            }
           
            Queue.remove(currentNode);
        }

        return "NOSOLUTION";




    }


    public static String AstarSearch(Node root,Boolean visualize)    {
    	int FoodTotal1=LLAPSearch.IntialState.get("unitPriceFood")*LLAPSearch.IntialState.get("foodUseBUILD1");
        int MaterialTotal1=LLAPSearch.IntialState.get("unitPriceMaterials")*LLAPSearch.IntialState.get("materialsUseBUILD1");
        int EnergyTotal1=LLAPSearch.IntialState.get("unitPriceEnergy")*LLAPSearch.IntialState.get("energyUseBUILD1");
        int Build1Price=LLAPSearch.IntialState.get("priceBUILD1");
        int Build1Total=FoodTotal1+MaterialTotal1+EnergyTotal1+Build1Price;
        

        int FoodTotal2=LLAPSearch.IntialState.get("unitPriceFood")*LLAPSearch.IntialState.get("foodUseBUILD2");
        int MaterialTotal2=LLAPSearch.IntialState.get("unitPriceMaterials")*LLAPSearch.IntialState.get("materialsUseBUILD2");
        int EnergyTotal2=LLAPSearch.IntialState.get("unitPriceEnergy")*LLAPSearch.IntialState.get("energyUseBUILD2");
        int Build2Price=LLAPSearch.IntialState.get("priceBUILD2");
        int Build2Total=FoodTotal2+MaterialTotal2+EnergyTotal2+Build2Price;

       PriorityQueue<Node> Queue = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return (o1.heuristic+o1.path_cost) - (o2.heuristic+o2.path_cost);
            }
        });
        ArrayList<Node> Ancestors = new ArrayList<Node>();
        Queue.add(root);
        int monetaryCost = 0;
        int nodesExpanded = 0;
        
        Ancestors.add(root);

        while(!Queue.isEmpty()){
        	if(visualize) {
       		 System.out.println("----------------------------------------------------------------------");
                System.out.println("Total Price for build1:"+Build1Total +" Total Energy for build1:"+LLAPSearch.IntialState.get("energyUseBUILD1")+" Total Food for build1: "+LLAPSearch.IntialState.get("foodUseBUILD1")+ " Total Materials for build1: "+LLAPSearch.IntialState.get("materialsUseBUILD1")+" Prosperity after build1:"+IntialState.get("prosperityBUILD1"));
                System.out.println("Total Price for build2:"+Build2Total +" Total Energy for build2:"+LLAPSearch.IntialState.get("energyUseBUILD2")+" Total Food for build2 : "+LLAPSearch.IntialState.get("foodUseBUILD2")+ " Total Materials for build2: "+LLAPSearch.IntialState.get("materialsUseBUILD2")+" Prosperity after build2:"+IntialState.get("prosperityBUILD2"));
                System.out.println("Amount per Energy order:"+LLAPSearch.IntialState.get("amountRequestEnergy"));
                System.out.println("Amount per Food order:"+LLAPSearch.IntialState.get("amountRequestFood"));
                System.out.println("Amount per Materials order:"+LLAPSearch.IntialState.get("amountRequestMaterials"));
                System.out.println("------------CurrentNode---------------");
                System.out.println("Parent Action :" + Queue.peek().ParentAction);
                System.out.println(Queue.peek().toString());
                System.out.println("----------------------------------------------------------------------");
       		
       	}
            nodesExpanded++;

                       if( Queue.peek().State.get("prosperity") >= 100 ){
            	String FinalPath=getPath(Queue.peek());
                monetaryCost = Queue.peek().State.get("money_spent");
                return FinalPath+";"+monetaryCost+";"+nodesExpanded;
            }

            Node currentNode = Queue.peek();
            for(int i =0 ; i<currentNode.operators.size();i++){
                if(currentNode.operators.get(i)=="RequestFood"){
                    Node A=RequestFood(currentNode);
                    boolean Repeated=checkAncestors(A,Ancestors);
                    if(!Repeated) {
                    	 Queue.add(A);
                         Ancestors.add(A);
                    }
                       


                } else if (currentNode.operators.get(i)=="RequestMaterials") {
                    Node B =RequestMaterials(currentNode);
                    boolean Repeated=checkAncestors(B,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(B);
                     Ancestors.add(B);
                   }


                } else if (currentNode.operators.get(i)=="RequestEnergy") {
                    Node C =RequestEnergy(currentNode);
                    boolean Repeated=checkAncestors(C,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(C);
                     Ancestors.add(C);
                   }

                } else if (currentNode.operators.get(i)=="BUILD1") {
                    Node D =BUILD1(currentNode);
                    boolean Repeated=checkAncestors(D,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(D);
                     Ancestors.add(D);
                   }

                }else if (currentNode.operators.get(i)=="BUILD2") {
                    Node E =BUILD2(currentNode);
                    boolean Repeated=checkAncestors(E,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(E);
                     Ancestors.add(E);
                   }

                }else if (currentNode.operators.get(i)=="WAIT") {
                    Node F =WAIT(currentNode);
                    boolean Repeated=checkAncestors(F,Ancestors);
                    if(!Repeated) {
                   	 Queue.add(F);
                        Ancestors.add(F);
                   }

                }
            }
           
            Queue.remove(currentNode);
        }

        return "NOSOLUTION";




    }

    public static boolean checkAncestors(Node node,ArrayList<Node> Ancestors){

    	int FoodTotal1=LLAPSearch.IntialState.get("unitPriceFood")*LLAPSearch.IntialState.get("foodUseBUILD1");
        int MaterialTotal1=LLAPSearch.IntialState.get("unitPriceMaterials")*LLAPSearch.IntialState.get("materialsUseBUILD1");
        int EnergyTotal1=LLAPSearch.IntialState.get("unitPriceEnergy")*LLAPSearch.IntialState.get("energyUseBUILD1");
        int Build1Price=LLAPSearch.IntialState.get("priceBUILD1");
        int Build1Total=FoodTotal1+MaterialTotal1+EnergyTotal1+Build1Price;
        

        int FoodTotal2=LLAPSearch.IntialState.get("unitPriceFood")*LLAPSearch.IntialState.get("foodUseBUILD2");
        int MaterialTotal2=LLAPSearch.IntialState.get("unitPriceMaterials")*LLAPSearch.IntialState.get("materialsUseBUILD2");
        int EnergyTotal2=LLAPSearch.IntialState.get("unitPriceEnergy")*LLAPSearch.IntialState.get("energyUseBUILD2");
        int Build2Price=LLAPSearch.IntialState.get("priceBUILD2");
        int Build2Total=FoodTotal2+MaterialTotal2+EnergyTotal2+Build2Price;
        for(int i=0;i<Ancestors.size();i++){

//            if(node.State.equals(Ancestors.get(i).State)&&(node.Request_Type.equals(Ancestors.get(i).Request_Type))&&(node.Wait_Period==Ancestors.get(i).Wait_Period)){
//            	System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//            	System.out.println("..........Current Node............");
//            	System.out.println(node.toString());
//            	System.out.println("..............");
//           	System.out.println("..........ANCESTOR NODE" +i+ "............");
//        	System.out.println(Ancestors.get(i).toString());
//        	System.out.println("..............");      
//
//        	System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        	if(node.State.equals(Ancestors.get(i).State) &&(node.Request_Type.equals(Ancestors.get(i).Request_Type))&&node.Wait_Period==Ancestors.get(i).Wait_Period){
//        		
//        		    System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//                    System.out.println("Total Price for build1:"+Build1Total +" Total Energy for build1:"+LLAPSearch.IntialState.get("energyUseBUILD1")+" Total Food for build1: "+LLAPSearch.IntialState.get("foodUseBUILD1")+ " Total Materials for build1: "+LLAPSearch.IntialState.get("materialsUseBUILD1")+" Prosperity after build1:"+IntialState.get("prosperityBUILD1"));
//                    System.out.println("Total Price for build2:"+Build2Total +" Total Energy for build2:"+LLAPSearch.IntialState.get("energyUseBUILD2")+" Total Food for build2 : "+LLAPSearch.IntialState.get("foodUseBUILD2")+ " Total Materials for build2: "+LLAPSearch.IntialState.get("materialsUseBUILD2")+" Prosperity after build2:"+IntialState.get("prosperityBUILD2"));
//
//                    System.out.println("..........Current Node............");
//                    System.out.println("Parent Action:"+node.ParentAction);
//                    System.out.println(node.toString());
//                    
//                    System.out.println("..............");
//                   	System.out.println("..........ANCESTOR NODE" +i+ "............");
//                    System.out.println("Parent Action of Ancestor:"+node.ParentAction);
//                	System.out.println(Ancestors.get(i).toString());
//     
//                	System.out.println("..............");
//                	System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        			
        		
        		
        
                return true;
            }
        }
        return false;
    }
    
    
        public static Node RequestMaterials(Node parent){
        	
            int FoodTotal1=IntialState.get("unitPriceFood")*IntialState.get("foodUseBUILD1");
            int MaterialTotal1=IntialState.get("unitPriceMaterials")*IntialState.get("materialsUseBUILD1");
            int EnergyTotal1=IntialState.get("unitPriceEnergy")*IntialState.get("energyUseBUILD1");
            int BuildTotal1=IntialState.get("priceBUILD1");
            int Total1=FoodTotal1+MaterialTotal1+EnergyTotal1+BuildTotal1;
            
            int FoodTotal2=IntialState.get("unitPriceFood")*IntialState.get("foodUseBUILD2");
            int MaterialTotal2=IntialState.get("unitPriceMaterials")*IntialState.get("materialsUseBUILD2");
            int EnergyTotal2=IntialState.get("unitPriceEnergy")*IntialState.get("energyUseBUILD2");
            int BuildTotal2=IntialState.get("priceBUILD2");
            int Total2=FoodTotal2+MaterialTotal2+EnergyTotal2+BuildTotal2;
            
            
            int FoodTotal=IntialState.get("unitPriceFood");
            int MaterialTotal=IntialState.get("unitPriceMaterials");
            int EnergyTotal=IntialState.get("unitPriceEnergy");
            int Total=FoodTotal+MaterialTotal+EnergyTotal;
            HashMap<String,Integer> NewState = new HashMap<>();
            NewState.put("prosperity", (parent.State.get("prosperity")));
            NewState.put("food",(parent.State.get("food")-1));
            NewState.put("materials",(parent.State.get("materials")-1));
            NewState.put("energy",(parent.State.get("energy")-1));
            NewState.put("money_spent",parent.State.get("money_spent")+Total);

           
            int heuristic=0;
            
            if(heuristicFlag==1) {
            	 float prosperity_distance = 100 - NewState.get("prosperity");
                 float maxPros = Integer.max(IntialState.get("prosperityBUILD1"), IntialState.get("prosperityBUILD2"));
                 float temp = (float)(prosperity_distance/maxPros);  
                 heuristic = Math.round(temp*(Integer.min(Total1, Total2)));
                 if(heuristic<0) {
              	   heuristic = 0;
                 }
            }
            else if(heuristicFlag==2) {
            	 float prosperity_distance = 100 - NewState.get("prosperity");
            	 
            	 float AverageFood = Math.max(NewState.get("food")-IntialState.get("foodUseBUILD2"),Math.max(NewState.get("food")-IntialState.get("foodUseBUILD1"), 0));
            	 float AverageMaterials = Math.max(NewState.get("materials")-IntialState.get("materialsUseBUILD1"),Math.max(NewState.get("materials")-IntialState.get("materialsUseBUILD2"), 0));
            	 float AverageEnergy = Math.max(NewState.get("energy")-IntialState.get("energyUseBUILD1"),Math.max(NewState.get("energy")-IntialState.get("energyUseBUILD2"), 0));

            	 float temp = (prosperity_distance)/(Math.max(AverageFood, Math.max(AverageEnergy, Math.max(AverageMaterials, 1))));
            	 
            	  heuristic = (int) (temp * (float) (Math.min(Total1, Total2))) ;
                if(heuristic<0) {
                	   heuristic = 0;
                   }
            }


            Node n = new Node(NewState,parent,(parent.depth+1),NewState.get("money_spent"),heuristic,true,IntialState.get("delayRequestMaterials")+1,"materials","RequestMaterials");
            n.FindOperators();
//            System.out.println("-------------------------------------------------");
//            System.out.println(n.toString());
            return n;

        
      

    }

        public static Node RequestEnergy(Node parent){
        	
            int FoodTotal1=IntialState.get("unitPriceFood")*IntialState.get("foodUseBUILD1");
            int MaterialTotal1=IntialState.get("unitPriceMaterials")*IntialState.get("materialsUseBUILD1");
            int EnergyTotal1=IntialState.get("unitPriceEnergy")*IntialState.get("energyUseBUILD1");
            int BuildTotal1=IntialState.get("priceBUILD1");
            int Total1=FoodTotal1+MaterialTotal1+EnergyTotal1+BuildTotal1;
            
            int FoodTotal2=IntialState.get("unitPriceFood")*IntialState.get("foodUseBUILD2");
            int MaterialTotal2=IntialState.get("unitPriceMaterials")*IntialState.get("materialsUseBUILD2");
            int EnergyTotal2=IntialState.get("unitPriceEnergy")*IntialState.get("energyUseBUILD2");
            int BuildTotal2=IntialState.get("priceBUILD2");
            int Total2=FoodTotal2+MaterialTotal2+EnergyTotal2+BuildTotal2;
        	
            int FoodTotal=IntialState.get("unitPriceFood");
            int MaterialTotal=IntialState.get("unitPriceMaterials");
            int EnergyTotal=IntialState.get("unitPriceEnergy");
            int Total=FoodTotal+MaterialTotal+EnergyTotal;
            HashMap<String,Integer> NewState = new HashMap<>();
            NewState.put("prosperity", (parent.State.get("prosperity")));
            NewState.put("food",(parent.State.get("food")-1));
            NewState.put("materials",(parent.State.get("materials")-1));
            NewState.put("energy",(parent.State.get("energy")-1));
            NewState.put("money_spent",parent.State.get("money_spent")+Total);
            
            int heuristic=0;
            
            if(heuristicFlag==1) {
             	 float prosperity_distance = 100 - NewState.get("prosperity");
                  float maxPros = Integer.max(IntialState.get("prosperityBUILD1"), IntialState.get("prosperityBUILD2"));
                  float temp = (float)(prosperity_distance/maxPros);  
                  heuristic = Math.round(temp*(Integer.min(Total1, Total2)));
                  if(heuristic<0) {
               	   heuristic = 0;
                  }
             }
            else if(heuristicFlag==2) {
             	 float prosperity_distance = 100 - NewState.get("prosperity");
             	 
             	 float AverageFood = Math.max(NewState.get("food")-IntialState.get("foodUseBUILD2"),Math.max(NewState.get("food")-IntialState.get("foodUseBUILD1"), 0));
             	 float AverageMaterials = Math.max(NewState.get("materials")-IntialState.get("materialsUseBUILD1"),Math.max(NewState.get("materials")-IntialState.get("materialsUseBUILD2"), 0));
             	 float AverageEnergy = Math.max(NewState.get("energy")-IntialState.get("energyUseBUILD1"),Math.max(NewState.get("energy")-IntialState.get("energyUseBUILD2"), 0));

             	 float temp = (prosperity_distance)/(Math.max(AverageFood, Math.max(AverageEnergy, Math.max(AverageMaterials, 1))));
             	 
             	  heuristic = (int) (temp * (float) (Math.min(Total1, Total2))) ;
                 if(heuristic<0) {
                 	   heuristic = 0;
                    }
             }
            
            Node n = new Node(NewState,parent,(parent.depth+1),NewState.get("money_spent"),heuristic,true,IntialState.get("delayRequestEnergy")+1,"energy","RequestEnergy");
            n.FindOperators();
//            System.out.println("-------------------------------------------------");
//            System.out.println(n.toString());
            return n;
        
    }

        public static Node RequestFood(Node parent){
        	
            int FoodTotal1=IntialState.get("unitPriceFood")*IntialState.get("foodUseBUILD1");
            int MaterialTotal1=IntialState.get("unitPriceMaterials")*IntialState.get("materialsUseBUILD1");
            int EnergyTotal1=IntialState.get("unitPriceEnergy")*IntialState.get("energyUseBUILD1");
            int BuildTotal1=IntialState.get("priceBUILD1");
            int Total1=FoodTotal1+MaterialTotal1+EnergyTotal1+BuildTotal1;
            
            int FoodTotal2=IntialState.get("unitPriceFood")*IntialState.get("foodUseBUILD2");
            int MaterialTotal2=IntialState.get("unitPriceMaterials")*IntialState.get("materialsUseBUILD2");
            int EnergyTotal2=IntialState.get("unitPriceEnergy")*IntialState.get("energyUseBUILD2");
            int BuildTotal2=IntialState.get("priceBUILD2");
            int Total2=FoodTotal2+MaterialTotal2+EnergyTotal2+BuildTotal2;
        	
            int FoodTotal=IntialState.get("unitPriceFood");
            int MaterialTotal=IntialState.get("unitPriceMaterials");
            int EnergyTotal=IntialState.get("unitPriceEnergy");
            int Total=FoodTotal+MaterialTotal+EnergyTotal;
            HashMap<String,Integer> NewState = new HashMap<>();
            NewState.put("prosperity", (parent.State.get("prosperity")));
            NewState.put("food",(parent.State.get("food")-1));
            NewState.put("materials",(parent.State.get("materials")-1));
            NewState.put("energy",(parent.State.get("energy")-1)); 
            NewState.put("money_spent",parent.State.get("money_spent")+Total);
            
            int heuristic=0;
            
            if(heuristicFlag==1) {
             	 float prosperity_distance = 100 - NewState.get("prosperity");
                  float maxPros = Integer.max(IntialState.get("prosperityBUILD1"), IntialState.get("prosperityBUILD2"));
                  float temp = (float)(prosperity_distance/maxPros);  
                  heuristic = Math.round(temp*(Integer.min(Total1, Total2)));
                  if(heuristic<0) {
               	   heuristic = 0;
                  }
             }
            else if(heuristicFlag==2) {
             	 float prosperity_distance = 100 - NewState.get("prosperity");
             	 
             	 float AverageFood = Math.max(NewState.get("food")-IntialState.get("foodUseBUILD2"),Math.max(NewState.get("food")-IntialState.get("foodUseBUILD1"), 0));
             	 float AverageMaterials = Math.max(NewState.get("materials")-IntialState.get("materialsUseBUILD1"),Math.max(NewState.get("materials")-IntialState.get("materialsUseBUILD2"), 0));
             	 float AverageEnergy = Math.max(NewState.get("energy")-IntialState.get("energyUseBUILD1"),Math.max(NewState.get("energy")-IntialState.get("energyUseBUILD2"), 0));

             	 float temp = (prosperity_distance)/(Math.max(AverageFood, Math.max(AverageEnergy, Math.max(AverageMaterials, 1))));
             	 
             	  heuristic = (int) (temp * (float) (Math.min(Total1, Total2))) ;
                 if(heuristic<0) {
                 	   heuristic = 0;
                    }
             }

          
            Node n = new Node(NewState,parent,(parent.depth+1),NewState.get("money_spent"),heuristic,true,(IntialState.get("delayRequestFood")+1),"food","RequestFood");
            n.FindOperators();
//            System.out.println("-------------------------------------------------");
//            System.out.println(n.toString());
            return n;
        

    }


        public static Node WAIT(Node parent){
        	
            int FoodTotal1=IntialState.get("unitPriceFood")*IntialState.get("foodUseBUILD1");
            int MaterialTotal1=IntialState.get("unitPriceMaterials")*IntialState.get("materialsUseBUILD1");
            int EnergyTotal1=IntialState.get("unitPriceEnergy")*IntialState.get("energyUseBUILD1");
            int BuildTotal1=IntialState.get("priceBUILD1");
            int Total1=FoodTotal1+MaterialTotal1+EnergyTotal1+BuildTotal1;
            
            int FoodTotal2=IntialState.get("unitPriceFood")*IntialState.get("foodUseBUILD2");
            int MaterialTotal2=IntialState.get("unitPriceMaterials")*IntialState.get("materialsUseBUILD2");
            int EnergyTotal2=IntialState.get("unitPriceEnergy")*IntialState.get("energyUseBUILD2");
            int BuildTotal2=IntialState.get("priceBUILD2");
            int Total2=FoodTotal2+MaterialTotal2+EnergyTotal2+BuildTotal2;
        	
            int FoodTotal=IntialState.get("unitPriceFood");
            int MaterialTotal=IntialState.get("unitPriceMaterials");
            int EnergyTotal=IntialState.get("unitPriceEnergy");
            int Total=FoodTotal+MaterialTotal+EnergyTotal;
            HashMap<String,Integer> NewState = new HashMap<>();
            NewState.put("prosperity", (parent.State.get("prosperity")));
            NewState.put("food",(parent.State.get("food")-1));
            NewState.put("materials",(parent.State.get("materials")-1));
            NewState.put("energy",(parent.State.get("energy")-1));    
            NewState.put("money_spent",(parent.State.get("money_spent")+Total));
            NewState.put("money_spent",parent.State.get("money_spent")+Total);
            
            int heuristic=0;
            
            if(heuristicFlag==1) {
             	 float prosperity_distance = 100 - NewState.get("prosperity");
                  float maxPros = Integer.max(IntialState.get("prosperityBUILD1"), IntialState.get("prosperityBUILD2"));
                  float temp = (float)(prosperity_distance/maxPros);  
                  heuristic = Math.round(temp*(Integer.min(Total1, Total2)));
                  if(heuristic<0) {
               	   heuristic = 0;
                  }
             }
            else if(heuristicFlag==2) {
             	 float prosperity_distance = 100 - NewState.get("prosperity");
             	 
             	 float AverageFood = Math.max(NewState.get("food")-IntialState.get("foodUseBUILD2"),Math.max(NewState.get("food")-IntialState.get("foodUseBUILD1"), 0));
             	 float AverageMaterials = Math.max(NewState.get("materials")-IntialState.get("materialsUseBUILD1"),Math.max(NewState.get("materials")-IntialState.get("materialsUseBUILD2"), 0));
             	 float AverageEnergy = Math.max(NewState.get("energy")-IntialState.get("energyUseBUILD1"),Math.max(NewState.get("energy")-IntialState.get("energyUseBUILD2"), 0));

             	 float temp = (prosperity_distance)/(Math.max(AverageFood, Math.max(AverageEnergy, Math.max(AverageMaterials, 1))));
             	 
             	  heuristic = (int) (temp * (float) (Math.min(Total1, Total2))) ;
                 if(heuristic<0) {
                 	   heuristic = 0;
                    }
             }


            
            Node n = new Node(NewState,parent,(parent.depth+1),NewState.get("money_spent"),heuristic,parent.Delivery,parent.Wait_Period,parent.Request_Type,"WAIT");
            n.FindOperators();
            return n;

        }


    public static Node BUILD1(Node parent){
        int FoodTotal1=IntialState.get("unitPriceFood")*IntialState.get("foodUseBUILD1");
        int MaterialTotal1=IntialState.get("unitPriceMaterials")*IntialState.get("materialsUseBUILD1");
        int EnergyTotal1=IntialState.get("unitPriceEnergy")*IntialState.get("energyUseBUILD1");
        int BuildTotal1=IntialState.get("priceBUILD1");
        int Total1=FoodTotal1+MaterialTotal1+EnergyTotal1+BuildTotal1;
        
        int FoodTotal2=IntialState.get("unitPriceFood")*IntialState.get("foodUseBUILD2");
        int MaterialTotal2=IntialState.get("unitPriceMaterials")*IntialState.get("materialsUseBUILD2");
        int EnergyTotal2=IntialState.get("unitPriceEnergy")*IntialState.get("energyUseBUILD2");
        int BuildTotal2=IntialState.get("priceBUILD2");
        int Total2=FoodTotal2+MaterialTotal2+EnergyTotal2+BuildTotal2;
        
        
        HashMap<String,Integer> NewState = new HashMap<>();
        NewState.put("prosperity", (parent.State.get("prosperity")+IntialState.get("prosperityBUILD1")));
        NewState.put("food",(parent.State.get("food")-IntialState.get("foodUseBUILD1")));
        NewState.put("materials",(parent.State.get("materials")-IntialState.get("materialsUseBUILD1")));
        NewState.put("energy",(parent.State.get("energy")-IntialState.get("energyUseBUILD1")));
        NewState.put("money_spent",(parent.State.get("money_spent")+Total1));
        
        NewState.put("money_spent",(parent.State.get("money_spent")+Total1));
        NewState.put("money_spent",parent.State.get("money_spent")+Total1);
        
        int heuristic=0;
        
        if(heuristicFlag==1) {
         	 float prosperity_distance = 100 - NewState.get("prosperity");
              float maxPros = Integer.max(IntialState.get("prosperityBUILD1"), IntialState.get("prosperityBUILD2"));
              float temp = (float)(prosperity_distance/maxPros);  
              heuristic = Math.round(temp*(Integer.min(Total1, Total2)));
              if(heuristic<0) {
           	   heuristic = 0;
              }
         }
        else if(heuristicFlag==2) {
          	 float prosperity_distance = 100 - NewState.get("prosperity");
          	 
          	 float AverageFood = Math.max(NewState.get("food")-IntialState.get("foodUseBUILD2"),Math.max(NewState.get("food")-IntialState.get("foodUseBUILD1"), 0));
          	 float AverageMaterials = Math.max(NewState.get("materials")-IntialState.get("materialsUseBUILD1"),Math.max(NewState.get("materials")-IntialState.get("materialsUseBUILD2"), 0));
          	 float AverageEnergy = Math.max(NewState.get("energy")-IntialState.get("energyUseBUILD1"),Math.max(NewState.get("energy")-IntialState.get("energyUseBUILD2"), 0));

          	 float temp = (prosperity_distance)/(Math.max(AverageFood, Math.max(AverageEnergy, Math.max(AverageMaterials, 1))));
          	 
          	  heuristic = (int) (temp * (float) (Math.min(Total1, Total2))) ;
              if(heuristic<0) {
              	   heuristic = 0;
                 }
          }
        
        Node n = new Node(NewState,parent,(parent.depth+1),NewState.get("money_spent"),heuristic,parent.Delivery,parent.Wait_Period,parent.Request_Type,"BUILD1");
        n.FindOperators();
        return n;

    }

    public static Node BUILD2(Node parent){
    	
        int FoodTotal1=IntialState.get("unitPriceFood")*IntialState.get("foodUseBUILD1");
        int MaterialTotal1=IntialState.get("unitPriceMaterials")*IntialState.get("materialsUseBUILD1");
        int EnergyTotal1=IntialState.get("unitPriceEnergy")*IntialState.get("energyUseBUILD1");
        int BuildTotal1=IntialState.get("priceBUILD1");
        int Total1=FoodTotal1+MaterialTotal1+EnergyTotal1+BuildTotal1;
    	
    	
        int FoodTotal2=IntialState.get("unitPriceFood")*IntialState.get("foodUseBUILD2");
        int MaterialTotal2=IntialState.get("unitPriceMaterials")*IntialState.get("materialsUseBUILD2");
        int EnergyTotal2=IntialState.get("unitPriceEnergy")*IntialState.get("energyUseBUILD2");
        int BuildTotal2=IntialState.get("priceBUILD2");
        int Total2=FoodTotal2+MaterialTotal2+EnergyTotal2+BuildTotal2;
        
        
        HashMap<String,Integer> NewState = new HashMap<>();
        NewState.put("prosperity", (parent.State.get("prosperity")+IntialState.get("prosperityBUILD2")));
        NewState.put("food",(parent.State.get("food")-IntialState.get("foodUseBUILD2")));
        NewState.put("materials",(parent.State.get("materials")-IntialState.get("materialsUseBUILD2")));
        NewState.put("energy",(parent.State.get("energy")-IntialState.get("energyUseBUILD2")));
        NewState.put("money_spent",(parent.State.get("money_spent")+Total2));;
        
        NewState.put("money_spent",(parent.State.get("money_spent")+Total2));
        NewState.put("money_spent",parent.State.get("money_spent")+Total2);
        
        int heuristic=0;
        
        if(heuristicFlag==1) {
         	 float prosperity_distance = 100 - NewState.get("prosperity");
              float maxPros = Integer.max(IntialState.get("prosperityBUILD1"), IntialState.get("prosperityBUILD2"));
              float temp = (float)(prosperity_distance/maxPros);  
              heuristic = Math.round(temp*(Integer.min(Total1, Total2)));
              if(heuristic<0) {
           	   heuristic = 0;
              }
         }
        else if(heuristicFlag==2) {
         	 float prosperity_distance = 100 - NewState.get("prosperity");
         	 
         	 float AverageFood = Math.max(NewState.get("food")-IntialState.get("foodUseBUILD2"),Math.max(NewState.get("food")-IntialState.get("foodUseBUILD1"), 0));
         	 float AverageMaterials = Math.max(NewState.get("materials")-IntialState.get("materialsUseBUILD1"),Math.max(NewState.get("materials")-IntialState.get("materialsUseBUILD2"), 0));
         	 float AverageEnergy = Math.max(NewState.get("energy")-IntialState.get("energyUseBUILD1"),Math.max(NewState.get("energy")-IntialState.get("energyUseBUILD2"), 0));

         	 float temp = (prosperity_distance)/(Math.max(AverageFood, Math.max(AverageEnergy, Math.max(AverageMaterials, 1))));
         	 
         	  heuristic = (int) (temp * (float) (Math.min(Total1, Total2))) ;
             if(heuristic<0) {
             	   heuristic = 0;
                }
         }
        
        Node n = new Node(NewState,parent,(parent.depth+1),NewState.get("money_spent"),heuristic,parent.Delivery,parent.Wait_Period,parent.Request_Type,"BUILD2");
        n.FindOperators();
        return n;

    }
}
