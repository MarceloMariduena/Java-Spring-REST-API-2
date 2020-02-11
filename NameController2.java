package Rest2;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
public class NameController2 {
    private static int id = 0;
    private ArrayList<Name2> names;

    public NameController2(){
        names = new ArrayList<Name2>(){
            {
                add(new Name2(id++, "Ricky"));
                add(new Name2(id++, "Julian"));
                add(new Name2(id++, "Bubbles"));
                add(new Name2(id++, "Corey"));
                add(new Name2(id++, "Trevor"));
            }
        };
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage(){
        return "The setup is working.\n" +
                "Go to localhost:8080/names to see the list of names.\n" +
                "Go to localhost:8080/names/{id} to select a name based on its index ID.\n" +
                "Go to localhost:8080/addName/{name} to add a new name to the list.\n" +
                "Head back to localhost:8080/names to see the updated list.";
    }

    @RequestMapping(value = "/names", method = RequestMethod.GET)
    public ArrayList<Name2> getNames(){
        return names;
    }

    @RequestMapping(value = "/names/{id}", method = RequestMethod.GET)
    public Name2 getName(@PathVariable int id){
        int found = findName(id);
        return names.get(found);
    }

    @RequestMapping(value = "/addName/{newName}", method = RequestMethod.POST)
    public Name2 addName(@PathVariable("newName") String newName){
        Name2 name = new Name2(id, newName);
        names.add(name);
        id++;
        return name;
    }

    @RequestMapping(value = "/addName", method = RequestMethod.POST)
    public Name2 addNameThroughBody(@RequestBody String newName){
        id++;
        Name2 nameObj = new Name2(id, newName);
        names.add(nameObj);
        return nameObj;
    }

    @RequestMapping(value = "/addNameObject", method = RequestMethod.POST)
    public Name2 addNameObjectThroughBody(@RequestBody Name2 name){
        id++;
        name.setID(id);
        names.add(name);
        return name;
    }

    @RequestMapping(value = "/updateName", method = RequestMethod.PUT)
    public Name2 updateName(@RequestBody Name2 name){
        int foundID = findName((int)name.getID());
        names.set(foundID, name);
        return name;
    }

    @RequestMapping(value = "/updateNameById/{id}", method = RequestMethod.PUT)
    public Name2 updateNameById(@RequestBody String message, @PathVariable int id){
        boolean found = false;
        Name2 name = new Name2(id, message);
        int foundID = findName(id);
        names.set(foundID, name);
        return name;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public String deleteName(@PathVariable int id){
        int found = findName(id);
        names.remove(found);
        return "ID " + id + " deleted.";
    }


    // TODO
    @RequestMapping(value = "acceptNameObject", method = RequestMethod.POST)
    public Name2 createName(@RequestBody Name2 name){
        boolean found =  false;
        for(Name2 n : names){
            if(n.getID() == name.getID()){
                found = true;
                n.setName(name.getName());
                break;
            }
        }
        if(!found) names.add(name);
        return name;
    }

    // TODO
    @RequestMapping(value = "acceptListOfNames", method = RequestMethod.POST)
    public List<Name2> acceptSingleNameObject(@RequestBody ArrayList<Name2> listOfNames){
        HashMap<Integer, String> nameHashes = new HashMap<Integer, String>();
        for(Name2 n : names){
            nameHashes.put(n.getID(), n.getName());
        }
        for(Name2 n : listOfNames){
            nameHashes.put(n.getID(), n.getName());
        }
        names = new ArrayList<Name2>();
        for(Map.Entry<Integer, String> n : nameHashes.entrySet()){
            names.add(new Name2(n.getKey(), n.getValue()));
        }
        return names;
    }

    private int findName(int id){
        boolean found = false;
        int foundID = 0;
        for (int index = 0; index < names.size(); index++){
            if (id == names.get(index).getID()){
                found = true;
                foundID = index;
            }
        }
        if (!found){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID " + id + " was not found.");
        }
        return foundID;
    }
}
