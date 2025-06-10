package WalkingThroughFiles;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

public class FileTreeWalker implements Runnable {
    static final Path END_OF_WORK = Paths.get("");
    private final BlockingQueue<Path> queue;
    private final Path startDir;

    FileTreeWalker(Path path, BlockingQueue<Path> queue){
        this.queue = queue;
        this.startDir = path;
    }

    @Override
    public void run(){
        try {
            walk(this.startDir);

            queue.put(END_OF_WORK);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void walk(Path startDir){
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(startDir)){
            for (Path path:ds){
                if (Files.isDirectory(path)){
                    walk(path);
                }else {
                    this.queue.put(path);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
