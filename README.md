# Android-VideoCompress
* 使用MediaCodec和MediaCodec.Callback对视频进行h264转码，音频进行mp4a-latm转码，minSDK为21，因为MediaCodec.Callback是5.0新出的API。
* 如果有需要兼容4.x的系统的话，可以移步到此项目：[支持4.2以上系统](https://github.com/fishwjy/VideoCompressor)。此项目我在实际开发中也遇到了一些问题，但是似乎其他人都没有遇到，因为我提的issues都没人知道怎么回事...

## 先上个图
<iframe height=500 width=500 src="https://github.com/robinvanPersie/Android-VideoCompress/blob/master/gif/1526903594183.gif"></iframe>

# 说明
* 项目中代码来源于google测试代码，同时在github上有另一个人将这部分测试代码抽离了出来，且他未做任何修改，在此先感谢下。查看原项目点击此：[原项目地址](https://github.com/mstorsjo/android-decodeencodetest)。&nbsp;&nbsp;（PS：原项目非Gradle构建）

* 我在经过一些研究和测试后，对demo增加了一些填坑和适配的改动，如果想直接拿来就能用的话，可以直接下载我的demo，具体改动会在下面详细列出。


# 改动
* 删除了测试用的断言。

* 增加了中断转码的方法。

* 增加了新的回调：onPrePare(), &nbsp; onSuccess(), &nbsp; onFail(), &nbsp; onProgress()。

* 增加了结果返回，因为所有的处理都在MediaCodec.Callback的回调方法里，原项目只有在处理成功后，或处理前就抛出异常这两种情况下能同步获取处理结果。如果在转码过程中抛出异常，原项目会直接crash，我将这种情况处理成了转码失败,会调用onFail()。

* 针对其他视频录制App和录屏软件录制的视频进行兼容。什么意思呢？原项目针对手机录制的视频进行转码目前我没有发现问题，但是对其他视频录制App和录屏软件录的视频转码后会出现<font color="#ff0000">加速播放</font>或<font color="#ff0000">跳帧</font>的情况。例如：faceu，无他相机（只试了这两个App），华为手机自带的屏幕录制功能。前两个App录制出的视频已经是h264的了，所以我的demo所做的事情只是重新设置了一下尺寸，而屏幕录制功能录的视频会有音频采样率的问题。我目前定义的规则是宽，高最大不超过&nbsp;**960**px。

* 针对华为手机进行的适配。<u>**这是两个转码项目都会有的问题！**</u>
  1. 问题描述：华为手机硬编码出的视频在iOS和macOS上播放会有一半的绿屏。
  2. 原因：问题出在华为的硬件上面，这个问题可能需要提前了解一下视频的结构，大致描述就是，正常的视频画面是由很多frame(帧)组成，而1个frame里是包含1个slice（片）的，当然也可能包含多个slice。问题就出在slice上，华为的硬件编码出的视频1个frame里是包含多个slice的（具体几个不清楚），而iOS和macOS都只能支持1个frame里包含1个slice的视频，如果包含多个slice就会出现一半绿屏的问题，而Android默认两种情况都是支持的。
  3. 解决：这里我做了机型的判断，华为的手机全部改为使用软编码（OMX.google.h264.encoder），我查看了xiaomi，魅族，华为，vivo手机的配置文件（在/system/etc目录下，要用adb命令pull出来），h264的软编码都是这个编码器，而且这个编码器里有google字符串，我初步断定这个应该是所有手机都会有的h264软编码器。
  
# 如何使用
关于如何使用，可以参考MainPresenter.java文件，还是比较简单易用的。
  
# 注意事项
<font color="ff0000">如果想同时转码2个或2个以上的视频，我特别建议还是不要这样做了，因为没法适配华为...是的，又是华为。</font>
在改动3里的配置文件里面还定义了编解码器的最大并发数，然后，华为PLC型号的手机并发数只有2，其他的手机如xiaomi，vivo，魅族，华为meta型号，并发数都大于5，甚至大于10，只有这一部手机是2。而SDK里也没有能获取并发数的API，所以保险起见，还是一次转码一个视频吧，万一出现了一部并发数只有1的手机呢。
<br/>
#### 要是觉得好用就麻烦star一下啦，有问题的要修改也欢迎fork啦。

  





