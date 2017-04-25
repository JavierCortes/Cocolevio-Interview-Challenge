import java.math.BigDecimal;
import java.util.*;

public class ProfitCalculations {
	
	public static void main(String[] args){
		ArrayList<String> companies = new ArrayList<String>();
		ArrayList<Integer> amount = new ArrayList<Integer>();
		ArrayList<Integer> price = new ArrayList<Integer>();
		int totalMaterial = 0;
		BigDecimal totalProfit = new BigDecimal(0);
		
		List<CompanyInfo> buyers = maximizeProfit(companies, amount, price, totalMaterial);
		for(CompanyInfo c: buyers){
			totalProfit.add(c.profit);
		}
		System.out.println(buyers + " Total Profit: " + totalProfit);
	}
	
	/**
	 * This solution assumes the company that came to us does not have to completely 
	 * fill a request for every potential buyer
	 * @param companies: The names of all potential buyers
	 * @param amount: The amount of material requested by buyers
	 * @param price: The amount of money each buyer is willing to pay
	 * @param totalMaterial: The total amount of material that can be sold
	 * @return a list of the companies we should sell to in order to maximize profits
	 */
	private static List<CompanyInfo> maximizeProfit(ArrayList<String> companies, 
			ArrayList<Integer> amount, ArrayList<Integer> price, int totalMaterial){
		
		List<CompanyInfo> buyers = new ArrayList<CompanyInfo>();
		List<CompanyInfo> companyPricePerAmount = new ArrayList<CompanyInfo>();
		
		for(int i = 0; i < companies.size(); i++){
			CompanyInfo comInfo = new CompanyInfo(companies.get(i), amount.get(i), price.get(i));
			companyPricePerAmount.add(comInfo);
		}
		
		companyPricePerAmount = sortDescending(companyPricePerAmount);
		
		Iterator<CompanyInfo> it = companyPricePerAmount.iterator();
    	while(totalMaterial > 0 && it.hasNext()){
    		CompanyInfo comp = it.next();
    		int requestedMaterial = comp.amount;
    		int soldMaterial = requestedMaterial;
    		
    		if((totalMaterial - requestedMaterial) >= 0){
    			totalMaterial -= requestedMaterial;
    		}else{
    			soldMaterial = totalMaterial;
    			totalMaterial -= soldMaterial;
    		}
    		
    		comp.setSold(soldMaterial);
    		buyers.add(comp);
    	}
		
		return buyers;
	}
	
	/**
	 * Merge-Sort: Time complexity n*log(n)
	 * @param list: The list of BigDecimals to be sorted
	 * @return a list of BigDecimals sorted in descending order
	 */
	private static List<CompanyInfo> sortDescending(List<CompanyInfo> list){
		if(list.size() <= 1){
			return list;
		}
		
		List<CompanyInfo> left = new ArrayList<CompanyInfo>();
		List<CompanyInfo> right = new ArrayList<CompanyInfo>();
		
		for(int i = 0; i < list.size(); i++){
			if(i < list.size()/2){
				left.add(list.get(i));
			}else right.add(list.get(i));
		}
		
		left = sortDescending(left);
		right = sortDescending(right);
		
		int leftIndex = 0;
		int rightIndex = 0;
		List<CompanyInfo> result = new ArrayList<CompanyInfo>();
		
		while(leftIndex < left.size() && rightIndex < right.size()){
			CompanyInfo leftComp = left.get(leftIndex);
			CompanyInfo rightComp = right.get(rightIndex);
			
			if(leftComp.pricePerAmount.compareTo(rightComp.pricePerAmount) >= 0){
				result.add(left.get(leftIndex));
				leftIndex++;
			}else{
				result.add(right.get(rightIndex));
				rightIndex++;
			}
		}
		
		while(leftIndex < left.size()){
			result.add(left.get(leftIndex));
			leftIndex++;
		}
		
		while(rightIndex < right.size()){
			result.add(right.get(rightIndex));
			rightIndex++;
		}
		
		return result;
	}
	
	private static class CompanyInfo{
		String name;
		int amount;
		BigDecimal pricePerAmount;
		BigDecimal profit;
		
		CompanyInfo(String name, int amount, int price){
			this.name = name;
			this.amount = amount;
			
			pricePerAmount = new BigDecimal(price/amount);
		}
		
		void setSold(int soldAmount){
			profit = pricePerAmount.multiply(new BigDecimal(soldAmount));
		}
		
		@Override
		public String toString(){
			return name;
		}
	}
}
