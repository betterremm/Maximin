public class NodeList {
    private Node head;

    public void addNode(int value){
        if(head == null){
            head = new Node(value);
        } else {
            Node temp = head;
            while(temp.next != null){
                temp = temp.next;
            }
            temp.next = new Node(value);
        }
    }

    public boolean removeNode(int value){
        boolean isRemoved = false;
        while(head != null && head.value == value){
            head = head.next;
            isRemoved = true;
        }
        Node temp = head;
        while(temp != null && temp.next != null){
            if(temp.next.value == value){
                temp.next = temp.next.next;
                isRemoved = true;
            } else {
                temp = temp.next;
            }
        }
        return isRemoved;
    }

    public void print(){
        Node temp = head;
        while(temp != null){
            System.out.print(temp.value + " ");
            temp = temp.next;
        }
        System.out.println("null");
    }


}
