import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

public class ProfitCalculations {
	
	/**
	 * This solution assumes the company that came to us does not have to completely 
	 * fill a request for every potential buyer
	 * @param companies: The names of all potential buyers
	 * @param amount: The amount of material requested by buyers
	 * @param price: The amount of money each buyer is willing to pay
	 * @param totalMaterial: The total amount of material that can be sold
	 */
	public static void maximizeProfit(List<String> companies, 
			List<BigDecimal> amount, List<BigDecimal> price, BigDecimal totalMaterial){
		
		List<CompanyInfo> buyers = new ArrayList<CompanyInfo>();
		List<CompanyInfo> companyPricePerAmount = new ArrayList<CompanyInfo>();
		
		//Acquire all the info for a given company and add it to a list
		for(int i = 0; i < companies.size(); i++){
			CompanyInfo cInfo = new CompanyInfo(companies.get(i), amount.get(i), price.get(i));
			companyPricePerAmount.add(cInfo);
		}
		
		//Sort CompanyInfo by ratio of price/amount using merge-sort
		companyPricePerAmount = sortDescending(companyPricePerAmount);
		
		//Fractional Knapsack: Sell as much as you can to the highest bidder
		Iterator<CompanyInfo> it = companyPricePerAmount.iterator();
    	while(totalMaterial.compareTo(new BigDecimal(0)) > 0 && it.hasNext()){
    		CompanyInfo comp = it.next();
    		BigDecimal requestedMaterial = comp.amount;
    		BigDecimal soldMaterial = requestedMaterial;
    		
    		if((totalMaterial.subtract(requestedMaterial)).compareTo(new BigDecimal(0)) >= 0){
    			totalMaterial = totalMaterial.subtract(requestedMaterial);
    		}else{
    			soldMaterial = totalMaterial;
    			totalMaterial = totalMaterial.subtract(soldMaterial);
    		}
    		
    		comp.setSold(soldMaterial);
    		buyers.add(comp);
    	}
    	
    	//Print the names of the companies to sell to and the profit generated
    	BigDecimal totalProfit = new BigDecimal(0);
		for(CompanyInfo cInfo: buyers){
			totalProfit = totalProfit.add(cInfo.profit);
		}
		System.out.println(buyers + " Total Profit: " + totalProfit);
	}
	
	/**
	 * Merge-Sort: Time complexity n*log(n)
	 * @param list: The list of CompanyInfos to be sorted
	 * @return a list of CompanyInfos sorted in descending order based on their
	 * pricePerAmount
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
		BigDecimal amount;
		BigDecimal pricePerAmount;
		BigDecimal profit;
		
		CompanyInfo(String name, BigDecimal amount, BigDecimal price){
			this.name = name;
			this.amount = amount;
			
			pricePerAmount = price.divide(amount, MathContext.DECIMAL32);
		}
		
		void setSold(BigDecimal soldAmount){
			profit = pricePerAmount.multiply(soldAmount);
		}
		
		@Override
		public String toString(){
			return name;
		}
	}
}
