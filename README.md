# Open Url

[![GitHub license](https://img.shields.io/github/license/duzhaokun123/OpenUrl?style=flat-square)](https://github.com/duzhaokun123/OpenUrl/blob/main/LICENSE)
![Android SDK min 33](https://img.shields.io/badge/Android%20SDK-%3E%3D%2033-brightgreen?style=flat-square&logo=android)
![Android SDK target 34](https://img.shields.io/badge/Android%20SDK-target%2034-brightgreen?style=flat-square&logo=android)

简单的工具试图提升 Android 打开链接体验

## 起因

有人提到了一个名为 "用其他应用打开浏览器"(`chenmc.open.with.specified.app`) 的应用 功能是在打开链接时选择特定的浏览器(比如淘宝的链接用淘宝打开(而不是先 chrome 然后再跳转到淘宝 同时后台下载淘宝))

但存在一些问题
- 图标太丑
- 没有传递原始 intent, extra 丢失
- 只支持目标是普通浏览器的打开(不知道怎么做到的)

## 使用

设为默认浏览器

## 效果

TODO

## TODO
- [ ] 更复杂的规则(path 匹配...)
- [ ] 应用"打开支持的链接"导入
- [ ] 国际化

## Thanks

[AOSP](https://source.android.com/)

[gson](https://github.com/google/gson)