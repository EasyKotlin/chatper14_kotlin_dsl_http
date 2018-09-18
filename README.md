第14章 使用 Kotlin  DSL
===

我们在前面的章节中，已经看到了 Kotlin DSL 的强大功能。例如Gradle 的配置文件 build.gradle （Groovy），以及前面我们涉及到的Gradle Script Kotlin（Kotlin）、Anko（Kotlin）等，都是 DSL。我们可以看出，使用DSL的编程风格，可以让程序更加简单干净、直观简洁。当然，我们也可以创建自己的 DSL。

本章就让我们一起来学习一下 Kotlin中 DSL的相关内容。



 # 《Kotlin极简教程》

> #### [点击这里 > *去京东商城购买阅读* ](https://item.jd.com/12181725.html)
> #### [点击这里 > *去天猫商城购买阅读* ](https://detail.tmall.com/item.htm?id=558540170670)

![image.png](http://upload-images.jianshu.io/upload_images/1233356-698bea5e85c345da.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


![image.png](http://upload-images.jianshu.io/upload_images/1233356-ca75424c61672818.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


```
出版社： 机械工业出版社
ISBN：9787111579939
版次：1
商品编码：12181725
品牌：机工出版
包装：平装
开本：16开
出版时间：2017-09-01
```



我们在上一章中已经看到了在 Android 中使用下面这样的 嵌套DSL 风格的代码来替代 XML 式风格的视图文件

```
        UI {
            // AnkoContext

            verticalLayout {
                padding = dip(30)
                var title = editText {
                    // editText 视图
                    id = R.id.todo_title
                    hintResource = R.string.title_hint
                }

                var content = editText {
                    id = R.id.todo_content
                    height = 400
                    hintResource = R.string.content_hint
                }
                button {
                    // button 视图
                    id = R.id.todo_add
                    textResource = R.string.add_todo
                    textColor = Color.WHITE
                    setBackgroundColor(Color.DKGRAY)
                    onClick { _ -> createTodoFrom(title, content) }
                }
            }
        }
```
相比 XML 风格的 DSL（XML 本质上讲也是一种 DSL），明显使用原生的编程语言（例如Kotlin）DSL 风格更加简单干净，也更加自由灵活。

Kotlin DSL 的编程风格是怎样的呢？以及其背后实现的原理是怎样的呢？下面就让我一起来探讨一下。


## DSL 是什么

DSL（Domain-Specific Language，领域特定语言）指的是专注于特定问题领域的计算机语言（领域专用语言）。不同于通用的计算机语言(GPL)，领域特定语言只用在某些特定的领域。 

“领域”可能特指某一产业，比如保险、教育、航空、医疗等；它也可能是指一种方法或者技术，比如JavaEE、.NET、数据库、服务、消息、架构以及领域驱动设计。开发DSL语言的目的在于，我们要以一种更优雅、更简洁的方式面对领域中的一些列挑战。之所以能够这样，是因为这个语言刚好够用于解决领域中唯一存在的一系列挑战，一点儿不多、也一点儿不少，刚刚好。

比如用来显示网页的HTML语言。更加典型的例子是Gradle，它基于Ant 和 Maven，使用基于Groovy的DSL 来声明项目构建配置 build.gradle，而不是传统的XML。

DSL 简单讲就是对一个特定问题 (受限的表达能力) 的方案模型的更高层次的抽象表达（领域语言），使其更加简单易懂 (容易理解的语义以及清晰的语义模型)。

DSL 只是问题解决方案模型的外部封装，这个模型可能是一个 API 库，也可能是一个完整的框架等等。DSL 提供了思考特定领域问题的模型语言，这使得我们可以更加简单高效地来解决问题。DSL 聚焦一个特定的领域，简单易懂，功能极简但完备。DSL 让我们理解和使用模型更加简易。


DSL 有内部 DSL 跟外部 DSL 之分。简单讲就是，像 Gradle、Anko 等都是我们使用通用编程语言（Java 和 Kotlin）创建的内部DSL， 

### 内部DSL

内部DSL是指与项目中使用的通用目的编程语言（Java、C#或Ruby）紧密相关的一类DSL。它基于通用编程语言实现。

例如，Rails框架被称为基于Ruby的DSL，用于管理Ruby开发的Web应用程序。Rails之所以被称为DSL，原因之一在于Rails应用了一些Ruby语言的特性，使得基于Rails编程看上去与基于通用目的的Ruby语言编程并不相同。

根据Martin Fowler和Eric Evans的观点，框架或者程序库的API是否满足内部DSL的关键特征之一就是它是否有一个流畅（fluent）的接口。这样，你就能够用短小的对象表达式去组织一个原本很长的表达式，使它读起来更加自然。




### 外部DSL

外部DSL跟通用编程语言（GPL）类似，但是外部DSL更加专注于特定领域。

创建外部DSL和创建一种通用的编程语言的过程是相似的，它可以是编译型或者解释型的。它具有形式化的文法，只允许使用良好定义的关键字和表达式类型。经过编译的DSL通常不会直接产生可执行的程序（但是它确实可以）。

大多数情况下，外部DSL可以转换为一种与核心应用程序的操作环境相兼容的资源，也可以转换为用于构建核心应用的通用目的编程语言。例如，Hibernate中使用的对象-关系映射文件，就是由外部DSL转换为资源的实例。

提示：关于 DSL 的详细介绍可以参考：《领域特定语言》（Martin Fowler）这本书。


## Kotlin 的 DSL 特性支持

许多现代语言为创建内部 DSL 提供了一些先进的方法, Kotlin 也不例外。 

在Kotlin 中创建 DSL ， 一般主要使用下面两个特性：

- 扩展函数、扩展属性
- 带接收者的 Lambda 表达式（高阶函数）

例如上面的示例的 `UI {...}`  的代码，我们举例简单说明如下

|函数名|           函数签名         |    备注说明    |
|--------|------------------------|---------------|
|UI| fun Fragment.UI(init: AnkoContext<Fragment>.() -> Unit):AnkoContext<T>|android.support.v4.app.Fragment的扩展函数; 入参 init 是一个带接收者的函数字面值, 我们直接传入的是一个 Lambda 表达式|
|verticalLayout|inline fun ViewManager.verticalLayout(init: _LinearLayout.() -> Unit): LinearLayout| android.view.ViewManager的扩展函数      |




## 使用kotlinx.html DSL 写前端代码

为了加深对 Kotlin DSL 实用性上的理解，我们本节再介绍一个 Kotlin 中关于
 HTML 的 DSL： kotlinx.html 。

kotlinx.html 是可在 Web 应用程序中用于构建 HTML 的 DSL。 它可以作为传统模板系统（例如JSP、FreeMarker等）的替代品。

kotlinx. html 分别提供了kotlinx-html-jvm 和 kotlinx-html-js库的DSL , 用于在 JVM 和浏览器 (或其他 javascript 引擎) 中直接使用 Kotlin 代码来构建 html, 直接解放了原有的 HTML 标签式的前端代码。这样，我们 也可以使用 Kotlin来先传统意义上的 HTML 页面了。 Kotlin Web 编程将会更加简单纯净。

提示： 更多关于kotlinx.html的相关内容可以参考它的 Github 地址 ：https://github.com/Kotlin/kotlinx.html

要使用 kotlinx.html 首先添加依赖

```
dependencies {
    def kotlinx_html_version = "0.6.3"
    compile "org.jetbrains.kotlinx:kotlinx-html-jvm:${kotlinx_html_version}"
    compile "org.jetbrains.kotlinx:kotlinx-html-js:${kotlinx_html_version}"
    ...
}
```
kotlinx.html 最新版本发布在 https://jcenter.bintray.com/ 仓库上，所以我们添加一下仓库的配置
```
repositories {
    maven { url 'https://jitpack.io' }
    mavenCentral()
    jcenter() // https://jcenter.bintray.com/ 仓库
    maven { url "https://repo.spring.io/snapshot" }
    maven { url "https://repo.spring.io/milestone" }
}
```

我们来写一个极简百度首页示例。这个页面的前端 HTML 代码如下：

```
<!DOCTYPE html>
<html lang=zh-CN>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name=viewport content="width=device-width,initial-scale=1">
    <title>百度一下</title>
    <link href="https://cdn.bootcss.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
    <link href="dsl.css" rel="stylesheet">
    <script src="dsl.js"></script>
</head>
<body>
<div class="container">
    <div class="ipad center">
        ![](http://upload-images.jianshu.io/upload_images/1233356-49a0fecdc8bfa9cf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
    </div>

    <form class="form">
        <input id="wd" class="form-control ipad">
        <button id="baiduBtn" type="submit" class="btn btn-primary form-control ipad">百度一下</button>
    </form>
</div>
</body>
</html>
```

其中，dsl.css文件内容如下
```
.ipad {
    margin: 10px
}

.center {
    text-align: center;
}
```


dsl.js 文件内容如下
```
$(function () {
    $('#baiduBtn').on('click', function () {
        var wd = $('#wd').val()
        window.open("https://www.baidu.com/s?wd=" + wd)
    })
})
```

上面我们是通常使用的 HTML+JS+CSS 的方式来写前端页面的方法。现在我们把 HTML 部分的代码用Kotlin 的 DSL  kotlinx.html 来重新实现一遍。

我们首先新建 Kotlin + Spring Boot 工程，然后直接来写 Kotlin 视图类HelloDSLView，代码如下：

```
package com.easy.kotlin.chapter14_kotlin_dsl.view

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.springframework.stereotype.Service

/**
 * Created by jack on 2017/7/22.
 */
@Service
class HelloDSLView {
    fun html(): String {
        return createHTML().html {
            head {
                meta {
                    charset = "utf-8"
                    httpEquiv = "X-UA-Compatible"
                    content = "IE=edge"
                }
                title("百度一下")
                link {
                    href = "https://cdn.bootcss.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css"
                    rel = "stylesheet"
                }
                script {
                    src = "https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"
                }
                link {
                    href = "dsl.css"
                    rel = "stylesheet"
                }
                script {
                    src = "dsl.js"
                }
            }

            body {
                div(classes = "container") {
                    div(classes = "ipad center") {
                        img {
                            src = "https://www.baidu.com/img/bd_logo1.png"
                            width = "270"
                            height = "129"
                        }
                    }

                    form(classes = "form") {
                        input(InputType.text, classes = "form-control ipad") {
                            id = "wd"
                        }
                        button(classes = "btn btn-primary form-control ipad") {
                            id = "baiduBtn"
                            type = ButtonType.submit
                            text("百度一下")

                        }
                    }

                }
            }
        }
    }
}
```
相比之下，我们使用 DSL 的风格要比原生 HTML 要简洁优雅。关键是，我们的这个 HTML 是用 Kotlin 写的，这也就意味着，我们的 HTML 代码不再是简单的静态的前端代码了。我们完全可以直接使用后端的接口返回数据来给 HTML 元素赋值，我们也完全具备了（当然是完全超越了）诸如 JSP、Freemarker 这样的视图模板引擎的各种判断、循环等的语法功能，因为我们直接使用的是一门强大的编程语言 Kotlin 来写的 HTML 代码 。

然后，我们就可以直接在控制器层的代码里直接调用我们的 Kotlin 视图代码了：

```
@Controller
class HelloDSLController {
    @Autowired
    var helloDSLView: HelloDSLView? = null

    @GetMapping("hello")
    fun helloDSL(model: Model): ModelAndView {
        model.addAttribute("hello", helloDSLView?.html())
        return ModelAndView("hello")
    }
}
```

为了简单起见，我们借用一下 Freemarker 来做视图解析引擎，但是它只负责原封不动地来传输我们的 Kotlin 视图代码。hello.ftl 代码如下：

```
${hello}
```
我们的源码目录如下

```
── src
    ├── main
    │   ├── java
    │   ├── kotlin
    │   │   └── com
    │   │       └── easy
    │   │           └── kotlin
    │   │               └── chapter14_kotlin_dsl
    │   │                   ├── Chapter14KotlinDslApplication.kt
    │   │                   ├── controller
    │   │                   │   └── HelloDSLController.kt
    │   │                   └── view
    │   │                       └── HelloDSLView.kt
    │   └── resources
    │       ├── application.properties
    │       ├── banner.txt
    │       ├── static
    │       │   ├── dsl.css
    │       │   ├── dsl.js
    │       │   └── hello.html
    │       └── templates
    │           └── hello.ftl
    └── test
        ├── java
        ├── kotlin
        │   └── com
        │       └── easy
        │           └── kotlin
        │               └── chapter14_kotlin_dsl
        │                   └── Chapter14KotlinDslApplicationTests.kt
        └── resources

```


然后，启动运行 SpringBoot 应用，浏览器访问  http://127.0.0.1:8888/hello ， 我们可以看到如下输出界面：


![螢幕快照 2017-07-23 03.53.07.png](http://upload-images.jianshu.io/upload_images/1233356-f36aff0846fa2d8c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这就是 DSL 的精妙之处。我们后面可以尝试使用 kotlinx.html 来写Kotlin 语言的前端代码了。在做 Web 开发的时候，我们通常是使用 HTML + 模板引擎（Velocity、JSP、Freemarker 等）来集成前后端的代码，这让我们有时候感到很尴尬，要学习模板引擎的语法，还得应对 前端HTML代码中凌乱的模板引擎标签、变量等片段代码。

使用 Kotlin DSL 来写 HTML 代码的情况将完全不一样了，我们将重拾前后端集成编码的乐趣（不再是模板引擎套前端 HTML，各种奇怪的 #、<#>、${} 模板语言标签），我们直接把 更加优雅简单的 DSL 风格的HTML 代码搬到了后端，同时HTML中的元素将直接跟后端的数据无缝交互，而完成这些的只是 Kotlin（当然，相应领域的 DSL 基本语义模型还是要学习一下）。

提示：本节项目源码： https://github.com/EasyKotlin/chapter14_kotlin_dsl





## 实现一个极简的http DSL

我们现在已经基本知道 Kotlin 中 DSL 的样子了。但是这些 DSL 都是怎样实现的呢？本节我们就通过实现一个极简的http DSL来学习创建 DSL 背后的基本原理。

在这里我们对 OkHttp 做一下简单的封装，实现一个类似 jquery 中的 Ajax 的 http 请求的DSL。

OkHttp 是一个成熟且强大的网络库，在Android源码中已经使用OkHttp替代原先的HttpURLConnection。很多著名的框架例如Picasso、Retrofit也使用OkHttp作为底层框架。

提示： 更多关于OkHttp 的使用可参考: http://square.github.io/okhttp/



### 创建 Kotlin Gradle 项目

我们首先使用 IDEA 创建 Kotlin Gradle 项目


![螢幕快照 2017-07-23 18.43.04.png](http://upload-images.jianshu.io/upload_images/1233356-d17ee2c191075c44.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


然后，在 build.gradle 里面配置依赖

```
    compile 'com.github.ReactiveX:RxKotlin:2.1.0'
    compile group: 'com.squareup.okhttp3', name: 'okhttp', version: '3.8.1'
    compile group: 'com.alibaba', name: 'fastjson', version: '1.2.35'
```

其中，RxKotlin是ReactiveX 框架对 Kotlin 语言的支持库。我们这里主要用RxKotlin来进行请求回调的异步处理。

我们使用的是  'com.github.ReactiveX:RxKotlin:2.1.0' ， 这个库是在 https://jitpack.io 上，所以我们在repositories配置里添加 jitpack 仓库

```
repositories {
    maven { url 'https://jitpack.io' }
    ...
}
```

### RxKotlin

ReactiveX是Reactive Extensions的缩写，一般简写为Rx，最初是LINQ的一个扩展，由微软的架构师Erik Meijer领导的团队开发，在2012年11月开源。


Rx扩展了观察者模式用于支持数据和事件序列。Rx是一个编程模型，目标是提供一致的编程接口，帮助开发者更方便的处理异步I/O（非阻塞）数据流。

Rx库支持.NET、JavaScript和C++ 。Rx近几年越来越流行，现在已经支持几乎全部的流行编程语言了。一个语言列表如下所示：

|Rx 支持的编程语言|  项目主页   |
|--------|---------|
|Java | [RxJava](https://github.com/ReactiveX/RxJava) ： https://github.com/ReactiveX/RxJava|
|JavaScript| [RxJS](https://github.com/ReactiveX/rxjs)：https://github.com/ReactiveX/rxjs
|C#| [Rx.NET](https://github.com/Reactive-Extensions/Rx.NET)：https://github.com/Reactive-Extensions/Rx.NET
|C#(Unity)| [UniRx](https://github.com/neuecc/UniRx)：https://github.com/neuecc/UniRx
|Scala| [RxScala](https://github.com/ReactiveX/RxScala)：https://github.com/ReactiveX/RxScala
|Clojure| [RxClojure](https://github.com/ReactiveX/RxClojure)：https://github.com/ReactiveX/RxClojure
|C++| [RxCpp](https://github.com/Reactive-Extensions/RxCpp)：https://github.com/Reactive-Extensions/RxCpp
|Lua| [RxLua](https://github.com/bjornbytes/RxLua)：https://github.com/bjornbytes/RxLua
|Ruby| [Rx.rb](https://github.com/Reactive-Extensions/Rx.rb)：https://github.com/Reactive-Extensions/Rx.rb
|Python:|[RxPY](https://github.com/ReactiveX/RxPY)：https://github.com/ReactiveX/RxPY
|Go| [RxGo](https://github.com/ReactiveX/RxGo)：https://github.com/ReactiveX/RxGo
|Groovy| [RxGroovy](https://github.com/ReactiveX/RxGroovy)：https://github.com/ReactiveX/RxGroovy
|JRuby| [RxJRuby](https://github.com/ReactiveX/RxJRuby)：https://github.com/ReactiveX/RxJRuby
|Kotlin| [RxKotlin](https://github.com/ReactiveX/RxKotlin)：https://github.com/ReactiveX/RxKotlin
|Swift| [RxSwift](https://github.com/kzaher/RxSwift)：https://github.com/kzaher/RxSwift
|PHP| [RxPHP](https://github.com/ReactiveX/RxPHP)：https://github.com/ReactiveX/RxPHP
|Elixir| [reaxive](https://github.com/alfert/reaxive)：https://github.com/alfert/reaxive
|Dart| [RxDart](https://github.com/ReactiveX/rxdart)：https://github.com/ReactiveX/rxdart





Rx的大部分语言库由ReactiveX这个组织负责维护。Rx 比较流行的库有RxJava/RxJS/Rx.NET等，当然未来RxKotlin也必将更加流行。


提示： Rx 的社区网站是： http://reactivex.io/ 。 Github 地址：https://github.com/ReactiveX/


### Http请求对象封装类

首先我们设计Http请求对象封装类如下
```
class HttpRequestWrapper {

    var url: String? = null

    var method: String? = null

    var body: RequestBody? = null

    var timeout: Long = 10

    internal var success: (String) -> Unit = {}
    internal var fail: (Throwable) -> Unit = {}

    fun success(onSuccess: (String) -> Unit) {
        success = onSuccess
    }

    fun error(onError: (Throwable) -> Unit) {
        fail = onError
    }
}
```

HttpRequestWrapper的成员变量和函数说明如下表

|成员           |           说明          |
|---|---|
| url|请求 url|
| method|请求方法，例如 Get、Post 等，不区分大小写|
| body|请求头，为了简单起见我们直接使用 OkHttp的RequestBody类型|
| timeout|超时时间ms，我们设置了默认值是10s|
| success|请求成功的函数变量|
| fail|请求失败的函数变量|
|fun success(onSuccess: (String) -> Unit)|请求成功回调函数|
|fun error(onError: (Throwable) -> Unit)|请求失败回调函数|



### http 执行引擎

我们直接调用 OkHttp 的 Http 请求 API 

```
private fun call(wrap: HttpRequestWrapper): Response {

    var req: Request? = null
    when (wrap.method?.toLowerCase()) {
        "get" -> req = Request.Builder().url(wrap.url).build()
        "post" -> req = Request.Builder().url(wrap.url).post(wrap.body).build()
        "put" -> req = Request.Builder().url(wrap.url).put(wrap.body).build()
        "delete" -> req = Request.Builder().url(wrap.url).delete(wrap.body).build()
    }

    val http = OkHttpClient.Builder().connectTimeout(wrap.timeout, TimeUnit.MILLISECONDS).build()
    val resp = http.newCall(req).execute()
    return resp
}
```
它返回请求的响应对象Response。

我们在`OkHttpClient.Builder().connectTimeout(wrap.timeout, TimeUnit.MILLISECONDS).build()`中设置超时时间的单位是 `TimeUnit.MILLISECONDS`。

我们通过`wrap.method?.toLowerCase()`处理请求方法的大小写的兼容。

### 使用 RxKotlin 完成请求响应的异步处理

我们首先新建一个数据发射源：一个可观察对象(Observable)，作为发射数据用

```
    val sender = Observable.create<Response>({
        e ->
        e.onNext(call(wrap))
    })
```

其中，e 的类型是 `io.reactivex.Emitter` （发射器），它的接口定义是
```
public interface Emitter<T> {
    void onNext(@NonNull T value);
    void onError(@NonNull Throwable error);
    void onComplete();
}
```
其方法功能简单说明如下：

|方法|功能|
|---|---|
|onNext|发射一个正常值数据（value）|
|onError|发射一个Throwable异常|
|onComplete|发射一个完成的信号|

这里，我们通过调用onNext方法，把 OkHttp 请求之后的响应对象Response 作为正常值发射出去。


然后我们再创建一个数据接收源：一个观察者（Observer）

```
    val receiver: Observer<Response> = object : Observer<Response> {
        override fun onNext(resp: Response) {
            wrap.success(resp.body()!!.string())
        }

        override fun onError(e: Throwable) {
            wrap.fail(e)
        }

        override fun onSubscribe(d: Disposable) {
        }

        override fun onComplete() {
        }

    }
```

receiver 的 onNext 函数接收 sender 发射过来的数据 Response， 然后我们在函数体内，调用这个响应对象，给 wrap.success 回调函数进行相关的赋值操作。同样的，onError 函数中也执行相应的赋值操作。

最后，通过 subscribe 订阅函数来绑定 sender 与 receiver 的关联：

```
sender.subscribe(receiver)
```

作为接收数据的 receiver （也就是 观察者 （Observer） ），对发送数据的 sender （也就是可被观察对象（ Observable）） 所发射的数据或数据序列作出响应。

这种模式可以极大地简化并发操作，因为它创建了一个处于待命状态的观察者，在未来某个时刻响应 sender 的通知，而不需要阻塞等待 sender 发射数据。这个很像协程中的通道编程模型。



### DSL主函数 ajax

 我们的ajax DSL主函数设计如下：
```
fun ajax(init: HttpRequestWrapper.() -> Unit) {
    val wrap = HttpRequestWrapper()
    wrap.init()
    doCall(wrap)
}
```
其中，参数`init: HttpRequestWrapper.() -> Unit` 是一个带接收者的函数字面量，它的类型是`init = Function1<com.kotlin.easy.HttpRequestWrapper, kotlin.Unit>`。  HttpRequestWrapper是扩展函数` init() `的接收者，点号 `.` 是扩展函数修饰符。

我们在函数体内直接调用了这个函数字面量 `wrap.init()` 。这样的写法可能比较难以理解，这个函数字面量 init 的调用实际上是 `init.invoke(wrap)` ，就是把传入 ajax 的函数参数直接传递给 wrap  。为了更简单的理解这个 init 函数的工作原理，我们通过把上面的 ajax 函数的代码反编译成对应的 Java 代码如下：

```
   public static final void ajax(@NotNull Function1 init) {
      Intrinsics.checkParameterIsNotNull(init, "init");
      HttpRequestWrapper wrap = new HttpRequestWrapper();
      init.invoke(wrap);
      doCall(wrap);
   }
```
也就是说，ajax 函数的一个更容易理解的写法是

```
fun ajax(init: HttpRequestWrapper.() -> Unit) {
    val wrap = HttpRequestWrapper()
    init.invoke(wrap)
    doCall(wrap)
}
```


我们在实际应用的时候，可以直接把 init 写成Lambda 表达式的形式，因为接收者类型HttpRequestWrapper 可以从上下文推断出来。

我们这样调用 ajax 函数：

```
ajax {
    url = testUrl
    method = "get"
    success {
        string ->
        println(string)
        Assert.assertTrue(string.contains("百度一下"))
    }
    error {
        e ->
        println(e.message)
    }
}
```

下面是几个测试代码示例：

```
package com.kotlin.easy

import com.alibaba.fastjson.JSONObject
import okhttp3.MediaType
import okhttp3.RequestBody
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by jack on 2017/7/23.
 */

@RunWith(JUnit4::class)
class KAjaxTest {

    @Test fun testHttpOnSuccess() {
        val testUrl = "https://www.baidu.com"
        ajax {
            url = testUrl
            method = "get"
            success {
                string ->
                println(string)
                Assert.assertTrue(string.contains("百度一下"))
            }
            error {
                e ->
                println(e.message)
            }
        }

    }

    @Test fun testHttpOnError() {
        val testUrl = "https://www2.baidu.com"

        ajax {
            url = testUrl
            method = "get"
            success {
                string ->
                println(string)
            }
            error {
                e ->
                println(e.message)
                Assert.assertTrue("connect timed out" == e.message)
            }
        }
    }


    @Test fun testHttpPost() {
        var json = JSONObject()
        json.put("name", "Kotlin DSL Http")
        json.put("owner", "Kotlin")
        val postBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString())
        ajax {
            url = "saveArticle"
            method = "post"
            body = postBody
            success {
                string ->
                println(string)
            }
            error {
                e ->
                println(e.message)
            }
        }
    }


    @Test fun testLambda() {
        val testUrl = "https://www.baidu.com"

        val init: HttpRequestWrapper.() -> Unit = {
            this.url = testUrl
            this.method = "get"
            this.success {
                string ->
                println(string)
                Assert.assertTrue(string.contains("百度一下"))
            }
            this.error {
                e ->
                println(e.message)
            }
        }
        ajax(init)
    }

```

到这里，我们已经完成了一个极简的 Kotlin Ajax DSL。

本节工程源码： https://github.com/EasyKotlin/chatper14_kotlin_dsl_http




## 本章小结

相比于Java，Kotlin对函数式编程的支持更加友好。Kotlin 的扩展函数和高阶函数（Lambda 表达式），为定义Kotlin DSL提供了核心的特性支持。

使用DSL的代码风格，可以让我们的程序更加直观易懂、简洁优雅。如果使用Kotlin来开发项目的话，我们完全可以去尝试一下。








