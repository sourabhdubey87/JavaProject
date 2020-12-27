package com.readWeb;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/*
 * Sourabh Dubey
 */
public class WordFrequancyfromWeb {
	 public static Set<String> uniqueURL = new HashSet<String>();
     public static String my_site;
     static Map<String, Integer> map = new HashMap<String, Integer>();
 
     public static void main(String[] args) throws IOException {
 
    	 WordFrequancyfromWeb obj = new WordFrequancyfromWeb();
         my_site = "https://www.314e.com/";
         obj.get_links("https://www.314e.com/", 4);
         System.out.println("Internal urls:");
         System.out.println(uniqueURL);
         updateFequancyMap();
         System.out.println("Top 10 frequent words");
         System.out.println(getTopFrequantWord(10));
         
         System.out.println("Top 10 frequent word pairs");
    	 findDuplicates();
     }
 
     /**
      * Method to get all inside links
      * @param url
      * @param depth
      */
     private void get_links(String url, int depth) {
         try {
             Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
             Elements links = doc.select("a");

             if (links.isEmpty() || depth<1) {
                return;
             }

             links.stream().map((link) -> link.attr("abs:href")).forEachOrdered((this_url) -> {
                 boolean add = uniqueURL.add(this_url);
                 if (add && this_url.contains(my_site)) {
                     //System.out.println(this_url);
                     get_links(this_url, depth -1);
                 }
             });
 
         } catch (IOException ex) {
 
         }
 
     }
     
     private static void updateFequancyMap() throws IOException {
 		Connection conn = null;
 		Document doc = null;
 		String result = null;
    	 for (String link : uniqueURL) {
 			//System.out.println("link : " + link);
 			if(link.contains("https://www.314e.com")) {
 				//Connecting to the web page
 				conn = Jsoup.connect(link);
 				//executing the get request
 				doc = conn.get();
 				//Retrieving the contents (body) of the web page
 				result = doc.body().text();
 				getWordFequancy(result);
 				//System.out.println("linkMap : " + map);
 			}
 			/*else {
 				System.out.println("Not including link " + link);
 			}*/

 		}
     }
     
     /**
      * To get word count map from given string 
      * @param body
      * @return
      */
     static Map<String, Integer> getWordFequancy(String body){
 		
 		String[] words = body.split(" ");
 		for(String word : words){
 			if(map.get(word)!=null){
 				map.put(word,map.get(word)+1);
 			}else{
 				map.put(word,1);
 			}
 		}
 		return map;
 	}
     
     /**
      * To map to descending order
     * @return 
      */
     private static HashMap<String, Integer> getTopFrequantWord(int limit) {
    	 List<Map.Entry<String, Integer> > list = 
    			 new LinkedList<Map.Entry<String, Integer> >(map.entrySet()); 

    	 // Sort the list 
    	 Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
    		 public int compare(Map.Entry<String, Integer> o1,  
    				 Map.Entry<String, Integer> o2) 
    		 { 
    			 return (o2.getValue()).compareTo(o1.getValue()); 
    		 } 
    	 }); 

    	 // put data from sorted list to hashmap  
    	 HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>(); 
    	 int i=0;
    	 for (Map.Entry<String, Integer> aa : list) { 
    		 if(i<limit){
    			 //System.out.println(aa.getKey() + " : " +  aa.getValue());
    			 temp.put(aa.getKey(), aa.getValue());
    			 i++;
    		 }
    	 }
    	 return temp;
     }
     
     /**
      * To find pair words
      * @param temp
      */
     public static void findDuplicates()
     {  
    	 map.entrySet().stream()
    	 .collect(Collectors.groupingBy(Entry::getValue,
    			 Collectors.mapping(Entry::getKey, Collectors.toList())))
    	 .entrySet().stream()
    	 .filter(e -> e.getValue().size() > 1)
    	 //.filter(e -> e.getKey() > 190)
    	 .sorted(new Comparator<Entry<Integer, List<String>>>() {
    		 public int compare(Entry o1,  
    				 Entry o2) 
    		 { 
    			 return ((Integer) o2.getKey()).compareTo((Integer) o1.getKey()); 
    		 } 
    	 })
    	 .limit(10)
    	 .forEach(System.out::println);
     }
}
