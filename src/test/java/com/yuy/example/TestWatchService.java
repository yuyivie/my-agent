package com.yuy.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.sun.jmx.mbeanserver.Util.cast;

public class TestWatchService {


  public static void main(String[] args) throws IOException {
    //需要监视的文件目录（注意：只能监听目录）
    String path = "d:/test";
    Path p = Paths.get(path);
    //创建监视服务类
    WatchService watchService = FileSystems.getDefault().newWatchService();

    //注册监控服务，监控新增、修改、删除事件，也可以只监控一个事件
    p.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE,
        StandardWatchEventKinds.ENTRY_CREATE);

    Thread thread = new Thread(() -> {
      for(;;){
        extracted(path, watchService);
      }

    });
    thread.setDaemon(false);
    thread.start();
    new File("d:/test/a.txt").createNewFile();
    new File("d:/test/a.txt").delete();
    new File("d:/test/test2").mkdir();
    new File("d:/test/test2/123.txt").createNewFile();
  }


  private static void extracted(String path, WatchService watchService) {
    try {
      WatchKey watchKey = watchService.poll(100, TimeUnit.MILLISECONDS);
//      System.out.println("key = " + watchKey);
      if (watchKey == null) {
        return;
      }
      Thread.sleep ( 200 );
      List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
        for (WatchEvent<?> event : watchEvents) {
          //对文件夹中的文件有操作，就会打印下列语句
          System.out.println(event.kind() + "事件：" + path + "/" + event.context());
          WatchEvent<Path> ev = cast(event);
        }
        watchKey.reset();

//        while (true) {
//          WatchKey watchKey = watchService.take();
//
//          List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
//          for (WatchEvent<?> event : watchEvents) {
//            //对文件夹中的文件有操作，就会打印下列语句
//            System.out.println(event.kind() + "事件：" + path + "/" + event.context());
//          }
//          watchKey.reset();
//        }

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

//  private boolean processEvents() throws InterruptedException,IOException {
//    String path = "d:/test";
//    Path p = Paths.get(path);
//
//
//    //创建监视服务类
//    WatchService watcher = FileSystems.getDefault().newWatchService();
//    // wait for key to be signaled
//    WatchKey key = watcher.poll(10, TimeUnit.MILLISECONDS);
//    //没有文件变更时，直接返回
//    if (key == null) {
//      return true;
//    }
//
//    //防止文件没有写完、下面获取到正在写入的文件。停止一会，文件无论如何也写完了。
//    //因为我们文件变更采用的是Watch文件监听。这里涉及到一个问题
//    //当java文件过大，有可能文件还没有写完全，已经被监听到了，很容易引发EOF，这里这里适当休眠一小会
//    //服务端测试，修改1万5000行代码写入监听无压力。
//    Thread.sleep ( 200 );
//
//    Path dir = keys.get(key);
//
//    if (dir == null) {
//      return true;
//    }
//
//    for (WatchEvent<?> event : key.pollEvents()) {
//      WatchEvent.Kind<?> kind = event.kind();
//
//      if (kind == OVERFLOW) {
//        continue;
//      }
//
//      // Context for directory entry event is the file name of entry
//      WatchEvent<Path> ev = cast(event);
//      Path name = ev.context();
//      //获取到当前变更的文件。
//      Path child = dir.resolve(name);
//
//      //核心逻辑，交给时间监视器来处理
//      dispatcher.add(ev, child);
//
//      if (kind == ENTRY_CREATE) {
//        try {
//          if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
//            //当文件首次监控到，需要先初始化一下监控的目录。手动调用Listener
//            recursiveFiles(child.toFile ().getAbsolutePath (),dispatcher,ev);
//            registerAll(child);
//          }
//        } catch (IOException x) {
//        }
//      }
//    }
//
//    boolean valid = key.reset();
//    if (!valid) {
//      keys.remove(key);
//      // all directories are inaccessible
//      if (keys.isEmpty()) {
//        return false;
//      }
//      if (classLoaderListeners.isEmpty()) {
//        for (WatchKey k : keys.keySet()) {
//          k.cancel();
//        }
//        return false;
//      }
//    }
//    return true;
//  }
}
