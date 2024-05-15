import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class Testing {
    private Repository repo1;
    private Repository repo2;

    // Occurs before each of the individual test cases
    // (creates new repos and resets commit ids)
    @BeforeEach
    public void setUp() {
        repo1 = new Repository("repo1");
        repo2 = new Repository("repo2");
        Repository.Commit.resetIds();
    }

    // TODO: Write your tests here!
    @Test
    @DisplayName("Should place newer commit from another repo at the front after synchronization.")
    public void testSynchronizeFront() {
        repo1.commit("Initial commit in Repo1");
        repo2.commit("New commit to be front in Repo1");

        repo1.synchronize(repo2);

        String expectedHeadId = "0";
        assertEquals(expectedHeadId, repo1.getRepoHead(),
                     "New commit should be the head after synchronization");
        assertEquals(0, repo2.getRepoSize(),
                     "Repo2 should be empty after synchronization");
    }

    @Test
    @DisplayName ("Should integrate a commit from another repo correctly into the middle of the repository.")
    public void testSynchronizeMiddle() {
        repo1.commit("First commit in Repo1");
        repo1.commit("Second commit in Repo1");
        repo2.commit("Commit to be in the middle of Repo1");

        repo1.synchronize(repo2);

        assertTrue(repo1.getHistory(3).contains("Commit to be in the middle of Repo1"),
                   "The middle commit from repo2 should be in repo1");
        assertEquals(0, repo2.getRepoSize(),
                     "Repo2 should be empty after synchronization");
    }

    @Test
    @DisplayName ("Should append commit from another repo to the end after synchronization.")
    public void testSynchronizeLast() {
        repo1.commit("Old commit in Repo1");
        repo2.commit("New last commit for Repo1");

        repo1.synchronize(repo2);

        String history = repo1.getHistory(2);
        assertTrue(history.endsWith("New last commit for Repo1"),
                   "The new last commit from repo2 should be the last commit in repo1");
        assertEquals(0, repo2.getRepoSize(),
                     "Repo2 should be empty after synchronization");
    }

    /////////////////////////////////////////////////////////////////////////////////
    // PROVIDED HELPER METHODS (You don't have to use these if you don't want to!) //
    /////////////////////////////////////////////////////////////////////////////////

    // Commits all of the provided messages into the provided repo, making sure timestamps
    // are correctly sequential (no ties). If used, make sure to include
    //      'throws InterruptedException'
    // much like we do with 'throws FileNotFoundException'. Example useage:
    //
    // repo1:
    //      head -> null
    // To commit the messages "one", "two", "three", "four"
    //      commitAll(repo1, new String[]{"one", "two", "three", "four"})
    // This results in the following after picture
    // repo1:
    //      head -> "four" -> "three" -> "two" -> "one" -> null
    //
    // YOU DO NOT NEED TO UNDERSTAND HOW THIS METHOD WORKS TO USE IT! (this is why documentation
    // is important!)
    public void commitAll(Repository repo, String[] messages) throws InterruptedException {
        // Commit all of the provided messages
        for (String message : messages) {
            int size = repo.getRepoSize();
            repo.commit(message);
            
            // Make sure exactly one commit was added to the repo
            assertEquals(size + 1, repo.getRepoSize(),
                         String.format("Size not correctly updated after commiting message [%s]",
                                       message));

            // Sleep to guarantee that all commits have different time stamps
            Thread.sleep(2);
        }
    }

    // Makes sure the given repositories history is correct up to 'n' commits, checking against
    // all commits made in order. Example useage:
    //
    // repo1:
    //      head -> "four" -> "three" -> "two" -> "one" -> null
    //      (Commits made in the order ["one", "two", "three", "four"])
    // To test the getHistory() method up to n=3 commits this can be done with:
    //      testHistory(repo1, 3, new String[]{"one", "two", "three", "four"})
    // Similarly, to test getHistory() up to n=4 commits you'd use:
    //      testHistory(repo1, 4, new String[]{"one", "two", "three", "four"})
    //
    // YOU DO NOT NEED TO UNDERSTAND HOW THIS METHOD WORKS TO USE IT! (this is why documentation
    // is important!)
    public void testHistory(Repository repo, int n, String[] allCommits) {
        int totalCommits = repo.getRepoSize();
        assertTrue(n <= totalCommits,
                   String.format("Provided n [%d] too big. Only [%d] commits",
                                 n, totalCommits));
        
        String[] nCommits = repo.getHistory(n).split("\n");
        
        assertTrue(nCommits.length <= n,
                   String.format("getHistory(n) returned more than n [%d] commits", n));
        assertTrue(nCommits.length <= allCommits.length,
                   String.format("Not enough expected commits to check against. " +
                                 "Expected at least [%d]. Actual [%d]",
                                 n, allCommits.length));
        
        for (int i = 0; i < n; i++) {
            String commit = nCommits[i];

            // Old commit messages/ids are on the left and the more recent commit messages/ids are
            // on the right so need to traverse from right to left
            int backwardsIndex = totalCommits - 1 - i;
            String commitMessage = allCommits[backwardsIndex];

            assertTrue(commit.contains(commitMessage),
                       String.format("Commit [%s] doesn't contain expected message [%s]",
                                     commit, commitMessage));
            assertTrue(commit.contains("" + backwardsIndex),
                       String.format("Commit [%s] doesn't contain expected id [%d]",
                                     commit, backwardsIndex));
        }
    }
}
