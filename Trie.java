import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Trie {
    private Node root;

    static class Node{
        Node[] children;
        boolean eow;

        public Node(){
            children=new Node[26];
            for(int i=0;i<26;i++){
                children[i]=null;
            }
            eow=false;
        }
    }

    public Trie(){
        this.root=new Node();
    }

    //insert in trie data structure
    public void insert(String word){
        Node curr=root;
        for(int i=0;i<word.length();i++){
            int idx=word.charAt(i)-'a';
            if(curr.children[idx]==null){
                curr.children[idx]=new Node();
            }
            curr=curr.children[idx];
        }
        curr.eow=true;
    }

    //searching in trie data structure
    public boolean search(String key){
        Node curr=root;
        for(int i=0;i<key.length();i++){
            int idx=key.charAt(i)-'a';
            if(curr.children[idx]==null)
                    return false;
            if(i==key.length()-1 && !curr.eow)
                    return false;
            curr=curr.children[idx];
        }
        return true;
    }

    //now autocomplete method:
    //Take a prefix as input.
    //Navigate to the node corresponding to the last character of the prefix.
    //From that node, recursively find all complete words that start with that prefix.
    public List<String> autocomplete(String prefix){
        List<String> result=new ArrayList<>();
        Node curr=root;
        //reach the end of prefix
        for(int i=0;i<prefix.length();i++){
            int idx=prefix.charAt(i)-'a';
            if(curr.children[idx]==null)
                return result; //no further suggestion for prefix are present
            curr=curr.children[idx];
        }
        //collect all suggestions
        collectSuggestions(curr,new StringBuilder(prefix),result);
        return result;
    }

    public void collectSuggestions(Node curr,StringBuilder prefix,List<String> result){
        if(curr.eow){
            result.add(prefix.toString());
        }

        for(int i=0;i<26;i++){
            if(curr.children[i]!=null){
                //get the character and append in prefix
                char ch=(char)('a'+i);
                prefix.append(ch);
                collectSuggestions(curr.children[i],prefix,result);
                //backtrack to find other suggestions
                prefix.deleteCharAt(prefix.length()-1);
            }
        }
    }

    public static void main(String[] args){
        Trie trie=new Trie();
        trie.insert("apple");
        trie.insert("app");
        trie.insert("application");
        trie.insert("apt");
        trie.insert("bat");
        trie.insert("banana");
        trie.insert("bandiana");
        trie.insert("car");
        trie.insert("casette");
        trie.insert("casino");
        trie.insert("came");
        trie.insert("camera");
        trie.insert("hi");
        trie.insert("his");
        trie.insert("hibiscus");
        trie.insert("history");

        Scanner sc=new Scanner(System.in);
        System.out.print("Enter a prefix: ");
        String prefix=sc.next();
        List<String> suggestions=trie.autocomplete(prefix);
        if (suggestions.isEmpty()) {
            System.out.println("No suggestions found. Do you want to add this word? (y/n)");
            if (sc.next().equalsIgnoreCase("y")) {
                trie.insert(prefix);
                System.out.println("Please insert a valid meaningful word only: ");
                String str=sc.next();
                System.out.println("Word \"" + str + "\" added to the Trie.");
            }
        }else {
            System.out.println("Suggested words for " + prefix + " are: " + suggestions);
        }
    }
}
