package cn.guankejian.server

enum class DnsProvider(val url: String) {
    DOT_PUB("https://doh.pub/dns-query"),
    ALI_DNS("https://dns.alidns.com/dns-query"),
    QIHU_360("https://doh.360.cn/dns-query"),
    GOOGLE("https://dns.google/dns-query"),
    ADGUARD("https://dns.adguard.com/dns-query"),
    QUAD9("https://dns.quad9.net/dns-query")
}
