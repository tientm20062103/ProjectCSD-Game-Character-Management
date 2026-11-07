package linkedlist;

import model.Team;

/**
 *
 * @author Minh Tien Tran
 */
public class TeamNode {
    Team info;
    TeamNode next;
 
    public TeamNode(Team x, TeamNode p) {
        info = x;
        next = p;
    }
     public TeamNode(Team x) {
       this(x, null);
    }
}
