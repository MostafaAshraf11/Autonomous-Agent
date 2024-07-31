package code;

import java.util.ArrayList;
import java.util.HashMap;
public class Node   {
    public  HashMap<String, Integer>State;
    public Node Parent;
    public  ArrayList<String> operators;
    public String Request_Type;
    public int depth;
    public int path_cost;
    public int heuristic;
    public  boolean Delivery;
    public int Wait_Period;
    public String ParentAction;
    public static int IDGenerator=0;
    public int NodeID;
    
    public Node(HashMap<String, Integer> State,Node Parent ,int depth,int path_cost,int heuristic,boolean Delivery,int Wait_Period,String Request_Type,String ParentAction){
        this.State = State;
        this.Parent = Parent;
        ArrayList<String> operators = new ArrayList<>();
        this.operators = operators;
        this.Delivery = Delivery;
        this.Wait_Period = Wait_Period;
        this.depth = depth;
        this.Request_Type=Request_Type;
        this.ParentAction = ParentAction;
        IDGenerator++;
        this.NodeID=IDGenerator;
        this.path_cost= path_cost;
        this.heuristic = heuristic;
       
       
    }

    public  void FindOperators(){

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
         
         if(Wait_Period!=1&&Wait_Period!=0){
             Delivery=true;
             Wait_Period--;
             operators.add("WAIT");
         }
         else{
             if(Delivery){
             	int NewState=0;
                 if(Request_Type=="food"){
                 	NewState=(State.get("food")+LLAPSearch.IntialState.get("amountRequestFood"));
                 }
                 if(Request_Type=="materials"){
                	 
                 	NewState=(State.get("materials")+LLAPSearch.IntialState.get("amountRequestMaterials"));
                 }
                 if(Request_Type=="energy"){
                 	NewState=(State.get("energy")+LLAPSearch.IntialState.get("amountRequestEnergy"));
                 }
                 if(NewState<50) {
                     State.put(Request_Type, NewState);
                 	
                 }else {
                     State.put(Request_Type, 50);
                 	
                 }
             }
             
             Delivery=false;
             Wait_Period=0;
             Request_Type="";
          	int FoodNum=(State.get("food")+LLAPSearch.IntialState.get("amountRequestFood"));
         	int MatNum=(State.get("materials")+LLAPSearch.IntialState.get("amountRequestMaterials"));
         	int EnergyNum=(State.get("energy")+LLAPSearch.IntialState.get("amountRequestEnergy"));

          	if(FoodNum<=50) {
                operators.add("RequestFood");
          	}
          	if(MatNum<=50) {
                operators.add("RequestMaterials");
          	}
          	if(EnergyNum<=50) {
                operators.add("RequestEnergy");
          	}
             

         }

       if((State.get("food")>= LLAPSearch.IntialState.get("foodUseBUILD1"))&& (State.get("materials")>= LLAPSearch.IntialState.get("materialsUseBUILD1")) && (State.get("energy")>= LLAPSearch.IntialState.get("energyUseBUILD1"))
               && ((100000-State.get("money_spent"))>= Build1Total ) ){
           operators.add("BUILD1");
       }


         if((State.get("food")>= LLAPSearch.IntialState.get("foodUseBUILD2"))&& (State.get("materials")>= LLAPSearch.IntialState.get("materialsUseBUILD2")) && (State.get("energy")>= LLAPSearch.IntialState.get("energyUseBUILD2"))
                 && ((100000-State.get("money_spent"))>= Build2Total ) ){
             operators.add("BUILD2");
         }
       



        if((State.get("food")<=0) || (State.get("energy") <= 0 )|| (State.get("materials")<=0 ) || (State.get("money_spent")>=100000)){
            operators.clear();
        }

    }

    @Override
	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("code.Node:\n");
        sb.append("NodeID:"+ NodeID+ "\n");
        if(Parent!=null) {
        	sb.append("Parent NodeID "+Parent.NodeID+ "\n");
        }
        sb.append("State: " + State + "\n");
//        sb.append("Parent: " + Parent + "\n");
        sb.append("Operators: " + operators + "\n");
        sb.append("Depth: " + depth + "\n");
        sb.append("Path cost: " + path_cost + "\n");
        sb.append("Heuristic: " + heuristic + "\n");
        sb.append("Delivery: " + Delivery + "\n");
        sb.append("Wait period: " + Wait_Period + "\n");
        sb.append("Request_type:"  + Request_Type + "\n");
        return sb.toString();
    }

}


