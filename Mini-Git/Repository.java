// Kourosh Ghahramani
// 04/29/2024
// CSE 123
// Programming Assignment 1: Mini-Git
// TA: Benoit Le

import java.util.*;
import java.text.SimpleDateFormat;

/**
 * The Repository class represents a version control repository that manages a series of commits.
 * It allows for operations such as committing changes, checking the size of the repository,
 * and synchronizing with other repositories to merge their commit histories.
 */
public class Repository {
    private String name;  // The name of the repository
    private Commit head;  // The head commit of the repository's commit chain

    /**
     * Constructs a new repository with the given name.
     * @param name The name of the repository.
     * @throws IllegalArgumentException If the name is null or empty.
     */
    public Repository(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    /**
     * Creates a new commit with the given message and adds it to the repository as the new head.
     * @param message The commit message.
     * @return The ID of the newly created commit.
     */
    public String commit(String message) {
        head = new Commit(message, head);
        return head.id;
    }

    /**
     * Retrieves the ID of the current head commit of the repository.
     * @return The ID of the head commit, or null if the repository is empty.
     */
    public String getRepoHead() {
        return (head != null) ? head.id : null;
    }

    /**
     * Counts the total number of commits in the repository.
     * @return The total number of commits.
     */
    public int getRepoSize() {
        int size = 0;
        Commit c = head;
        while (c != null) {
            size++;
            c = c.past;
        }
        return size;
    }

    /**
     * Provides a string representation of the repository, showing its name and current head commit.
     * @return A string detailing the repository's name and head commit.
     */
    @Override
    public String toString() {
        return name + (head == null ? " - No commits" : " - Current head: " + head);
    }

    /**
     * Checks if a commit with the specified ID exists within the repository.
     * @param targetId The ID of the commit to search for.
     * @return true if the commit exists, false otherwise.
     */
    public boolean contains(String targetId) {
        Commit c = head;
        while (c != null) {
            if (c.id.equals(targetId)) {
                return true;
            }
            c = c.past;
        }
        return false;
    }

    /**
     * Removes a commit with the specified ID from the repository, if it exists.
     * @param targetId The ID of the commit to remove.
     * @return true if the commit was removed successfully, false if no such commit exists.
     */
    public boolean drop(String targetId) {
        if (head == null) return false;
        if (head.id.equals(targetId)) {
            head = head.past;
            return true;
        }
        Commit current = head;
        while (current.past != null) {
            if (current.past.id.equals(targetId)) {
                current.past = current.past.past;
                return true;
            }
            current = current.past;
        }
        return false;
    }

    /**
    * Retrieves a history of commit messages up to a specified number from the head,
    * formatted as a string. If the specified number exceeds the actual number of commits 
    * in the repository, it returns as many commit messages as available in the repository.
    * @param n The number of commit messages to retrieve.
    * @return A string containing the most recent 'n'
    * commit messages, or fewer if the repository size is smaller than 'n'.
    * @throws IllegalArgumentException If 'n' is less than or equal to zero.
     */
    public String getHistory(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        String history = "";
        Commit current = head;
        while (current != null && n > 0) {
            history += current.toString() + "\n";
            current = current.past;
            n--;
        }
        return history.trim();
    }

    /**
    * Merges another repository's commit history into this repository, sorting by timestamp.
    * Ensures all commits from the other repository are integrated in chronological order and 
    * clears the other repository after merging. Handles cases where one or both repositories 
    * might initially be empty.
    *
    * @param other The repository to merge into this one.
    */
    public void synchronize(Repository other) {
        Commit current = this.head;
        Commit otherCurrent = other.head;
        Commit sortedHead = null;
        Commit lastSorted = null;

        while (current != null && otherCurrent != null) {
            if (current.timeStamp >= otherCurrent.timeStamp) {
                if (lastSorted == null) {
                    sortedHead = current;
                } else {
                    lastSorted.past = current;
                }
                lastSorted = current;
                current = current.past;
            } else {
                if (lastSorted == null) {
                    sortedHead = otherCurrent;
                } else {
                    lastSorted.past = otherCurrent;
                }
                lastSorted = otherCurrent;
                otherCurrent = otherCurrent.past;
            }
        }

        if (current != null) {
            if (lastSorted != null) {
                lastSorted.past = current;
            } else {
                sortedHead = current;
            }
        } else if (otherCurrent != null) {
            if (lastSorted != null) {
                lastSorted.past = otherCurrent;
            } else {
                sortedHead = otherCurrent;
            }
        }

        this.head = sortedHead;
        other.head = null;
    }



    

   













    /**
     * DO NOT MODIFY
     * A class that represents a single commit in the repository.
     * Commits are characterized by an identifier, a commit message,
     * and the time that the commit was made. A commit also stores
     * a reference to the immediately previous commit if it exists.
     *
     * Staff Note: You may notice that the comments in this 
     * class openly mention the fields of the class. This is fine 
     * because the fields of the Commit class are public. In general, 
     * be careful about revealing implementation details!
     */
    public class Commit {

        private static int currentCommitID;

        /**
         * The time, in milliseconds, at which this commit was created.
         */
        public final long timeStamp;

        /**
         * A unique identifier for this commit.
         */
        public final String id;

        /**
         * A message describing the changes made in this commit.
         */
        public final String message;

        /**
         * A reference to the previous commit, if it exists. Otherwise, null.
         */
        public Commit past;

        /**
         * Constructs a commit object. The unique identifier and timestamp
         * are automatically generated.
         * @param message A message describing the changes made in this commit.
         * @param past A reference to the commit made immediately before this
         *             commit.
         */
        public Commit(String message, Commit past) {
            this.id = "" + currentCommitID++;
            this.message = message;
            this.timeStamp = System.currentTimeMillis();
            this.past = past;
        }

        /**
         * Constructs a commit object with no previous commit. The unique
         * identifier and timestamp are automatically generated.
         * @param message A message describing the changes made in this commit.
         */
        public Commit(String message) {
            this(message, null);
        }

        /**
         * Returns a string representation of this commit. The string
         * representation consists of this commit's unique identifier,
         * timestamp, and message, in the following form:
         *      "[identifier] at [timestamp]: [message]"
         * @return The string representation of this collection.
         */
        @Override
        public String toString() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(timeStamp);

            return id + " at " + formatter.format(date) + ": " + message;
        }

        /**
        * Resets the IDs of the commit nodes such that they reset to 0.
        * Primarily for testing purposes.
        */
        public static void resetIds() {
            Commit.currentCommitID = 0;
        }
    }
}
