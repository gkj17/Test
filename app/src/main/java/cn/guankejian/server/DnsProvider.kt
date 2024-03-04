package cn.guankejian.server

enum class DnsProvider(val url: String) {
    DOT_PUB("https://doh.pub/dns-query"),
    ALI_DNS("https://dns.alidns.com/dns-query"),
    QIHU_360("https://doh.360.cn/dns-query"),
    GOOGLE("https://dns.google/dns-query"),
    ADGUARD("https://dns.adguard.com/dns-query"),
    QUAD9("https://dns.quad9.net/dns-query"),
    CLOUDFLARE("https://cloudflare-dns.com/dns-query"),//
    MOZILLA("https://mozilla.cloudflare-dns.com/dns-query"),//
    SWITCH("https://public.dns.iij.jp/dns-query"),//
    NEXTDNS("https://dns.nextdns.io/dns-query"),//
    OPEN_DNS("https://doh.opendns.com/dns-query"),//
    LIBREDNS("https://doh.libredns.gr/dns-query"),//
    COMCAST("https://doh.xfinity.com/dns-query"),//
    CLEANBROWSING("https://doh.cleanbrowsing.org/doh/family-filter"),

}
