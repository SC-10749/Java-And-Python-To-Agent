<script setup>
import { computed, onBeforeUnmount, onMounted, ref } from "vue";

const adminPages = [
  ["dashboard", "增长总览", "◫"],
  ["advertising", "广告投放", "↗"],
  ["registration", "注册转化", "◎"],
  ["funnel", "增长漏斗", "▽"],
  ["segments", "用户分群", "◌"],
  ["automation", "自动化运营", "⌘"],
  ["rules", "营销规则", "≡"],
  ["rewards", "奖励审核", "✓"],
  ["coupons", "优惠券", "◇"],
  ["checkin", "签到积分", "+"],
  ["referral", "邀请裂变", "↗"],
  ["groupbuy", "拼团活动", "◉"],
  ["seckill", "秒杀活动", "◷"],
  ["limited-coupon", "限时券", "⌛"],
  ["membership", "会员运营", "♙"],
  ["risk", "风控审核", "!"],
  ["service", "RAG 客服", "◔"],
  ["knowledge", "知识库", "▤"],
  ["feedback", "回答评价", "★"],
  ["tickets", "人工工单", "□"]
].map(([key, label, icon]) => ({ key, label, icon }));

const clientPages = [
  ["home", "我的首页", "⌂"],
  ["newbie", "新人权益", "✦"],
  ["checkin", "签到积分", "+"],
  ["invite", "邀请好友", "↗"],
  ["deals", "拼团秒杀", "◷"],
  ["member", "会员中心", "♙"],
  ["service", "在线客服", "◔"],
  ["my-coupons", "我的优惠券", "◇"]
].map(([key, label, icon]) => ({ key, label, icon }));

const overviewMetrics = [
  ["广告注册", "1,286", "较上期 +8.4%", "注册率 24.7%"],
  ["新人券领取", "986", "领取率 76.7%", "核销中 632 张"],
  ["首单转化", "428", "较上期 +11.2%", "首单率 33.3%"],
  ["7 日复购", "18.6%", "较上期 +2.1%", "会员贡献 41.5%"]
].map(([label, value, trend, note]) => ({ label, value, trend, note }));
const clientMetrics = [
  ["可用优惠券", "3", "1 张即将过期", "最高立减 ¥28"],
  ["连续签到", "6 天", "明日可领 12 积分", "本周排名 126"],
  ["我的积分", "1,280", "可兑换 3 项权益", "今日可再得 30 分"],
  ["会员成长值", "680", "距银卡还差 120", "本月复购券已发放"]
].map(([label, value, trend, note]) => ({ label, value, trend, note }));
const campaigns = [
  ["新人首单优惠活动", "新人优惠券", "986", "428", "¥ 6.20", "43.4%", "进行中"],
  ["连续签到积分任务", "签到积分", "1,126", "684", "¥ 1.80", "60.7%", "进行中"],
  ["邀请好友双向奖励", "邀请裂变", "364", "128", "¥ 12.40", "35.2%", "需关注"],
  ["周末 2 人拼团", "拼团秒杀", "212", "76", "¥ 15.60", "35.8%", "进行中"],
  ["晚间限时领券", "限时券", "518", "197", "¥ 4.80", "38.0%", "进行中"]
].map(([name, type, participants, firstOrders, rewardCost, rate, status]) => ({ name, type, participants, firstOrders, rewardCost, rate, status }));

const growthFlow = [
  ["01", "广告投放", "advertising"],
  ["02", "用户注册", "registration"],
  ["03", "新人优惠券", "coupons"],
  ["04", "签到 / 积分", "checkin"],
  ["05", "邀请裂变", "referral"],
  ["06", "拼团 / 秒杀", "groupbuy"],
  ["07", "会员召回", "membership"]
].map(([index, title, target]) => ({ index, title, target }));

const riskAlerts = [
  ["device", "同设备多账号领取新人券", "发现 6 个新账号使用同一设备指纹领取首单券，已进入拦截队列。", "red"],
  ["referral", "邀请奖励出现异常聚集", "3 个邀请链路在短时间内完成注册与首单，需要复核真实关系。", "orange"],
  ["seckill", "秒杀订单触发限购规则", "检测到高频下单与收货信息重复，已暂缓发货并等待审核。", "orange"]
].map(([id, title, description, tone]) => ({ id, title, description, tone }));

const funnelStages = [
  ["广告到达", "5,204", "100%", "广告素材与落地页"],
  ["完成注册", "1,286", "24.7%", "落地页注册"],
  ["领取新人券", "986", "76.7%", "新人首单券"],
  ["完成首单", "428", "43.4%", "拼团 / 秒杀 / 直接购买"],
  ["7 日复购", "239", "55.8%", "会员券与召回任务"]
].map(([name, value, rate, note]) => ({ name, value, rate, note }));

const segments = [
  ["new-users", "新注册未领券", "注册 24 小时内且未领取新人券", "318", "新人券提醒"],
  ["coupon-pending", "领券未下单", "领取新人券后 24 小时未完成首单", "227", "限时券 / 拼团"],
  ["active-invite", "高活跃邀请者", "近 7 天成功邀请 ≥ 2 人", "86", "邀请加码奖励"],
  ["silent-members", "沉默会员", "30 天未访问且会员等级 ≥ 银卡", "143", "复购召回券"]
].map(([id, name, rule, count, strategy]) => ({ id, name, rule, count, strategy }));

const automationFlows = ref([
  ["register-coupon", "注册后新人券引导", "完成注册 → 发新人券 → 等待 12 小时 → 未下单提醒", "1,286", true],
  ["coupon-convert", "领券未下单催化", "领取新人券 → 等待 20 小时 → 发 2 小时限时券", "227", true],
  ["silent-recall", "沉默会员召回", "30 天未访问 → 发复购券 → 3 天后检查是否回访", "143", false]
].map(([id, name, steps, audience, enabled]) => ({ id, name, steps, audience, enabled })));

const marketingRules = ref([
  ["newbie-coupon", "新人券发放", "首次注册 + 未命中设备黑名单", "发放 ¥28 新人券", true],
  ["checkin-points", "连续签到积分", "当天未签到 + 连续签到不超过 30 天", "发放 10–30 积分", true],
  ["invite-reward", "邀请双向奖励", "好友完成注册且首单支付成功", "双方各发 ¥12 券", true],
  ["seckill-limit", "秒杀限购", "同设备 / 同账号 / 同地址未超过限购", "允许锁定 1 件库存", true]
].map(([id, name, condition, outcome, enabled]) => ({ id, name, condition, outcome, enabled })));

const rewardRecords = ref([
  ["RW20260907001", "林晓敏", "新人首单券 ¥28", "优惠券", "2026-09-07 10:18", "已发放", "正常"],
  ["RW20260907002", "周元昊", "连续签到 20 积分", "积分", "2026-09-07 10:11", "已发放", "正常"],
  ["RW20260907003", "吴雨桐", "邀请好友奖励 ¥12", "优惠券", "2026-09-07 09:56", "待审核", "同设备风险"],
  ["RW20260907004", "陈子墨", "秒杀成功成长值 30", "成长值", "2026-09-07 09:42", "待审核", "高频下单"]
].map(([id, user, reward, type, time, status, risk]) => ({ id, user, reward, type, time, status, risk })));

const knowledgeDocuments = ref([
  ["doc-newbie", "新人优惠券活动规则.pdf", "PDF", "24 个知识片段", "2026-09-08 09:32", "已发布"],
  ["doc-checkin", "签到与积分说明.docx", "DOCX", "18 个知识片段", "2026-09-08 09:18", "已发布"],
  ["doc-deals", "拼团秒杀常见问题.md", "MD", "16 个知识片段", "2026-09-07 16:45", "已发布"],
  ["doc-member", "会员权益与召回策略.pdf", "PDF", "31 个知识片段", "2026-09-07 14:20", "待复核"]
].map(([id, name, format, chunks, updatedAt, status]) => ({ id, name, format, chunks, updatedAt, status })));
const knowledgeUpload = ref(null);
const customerQuestion = ref("");
const customerSending = ref(false);
const customerMessages = ref([
  { id: "welcome", role: "assistant", text: "你好，我是 AdGrowth 智能客服。我可以帮你查询新人券、签到积分、拼团秒杀和会员权益。", time: "现在", sources: [] },
  { id: "coupon", role: "user", text: "新人优惠券怎么领？", time: "10:32" },
  { id: "coupon-answer", role: "assistant", text: "完成注册后，新人首单券会自动发放到「我的优惠券」。这张券满 ¥99 可减 ¥28，有效期 7 天；如果暂未看到，可以稍等片刻后刷新优惠券列表。", time: "10:32", sources: ["新人优惠券活动规则", "优惠券发放规则"] }
]);
const serviceSuggestions = ["新人优惠券怎么领？", "签到积分可以提现吗？", "拼团失败后会退款吗？", "会员等级怎么提升？"];
const knowledgeGaps = ref([
  { id: "gap-seckill-stock", question: "秒杀订单取消后，库存多久返还？", detail: "近 7 天被问 12 次，当前无匹配文档。", score: "低", status: "待补充", count: 12 },
  { id: "gap-group-reward", question: "拼团团长奖励何时到账？", detail: "近 7 天被问 8 次，检索置信度不足。", score: "低", status: "待补充", count: 8 },
  { id: "gap-coupon-stack", question: "会员券能否与限时券叠加？", detail: "近 7 天被问 6 次，需要活动规则确认。", score: "中", status: "待确认", count: 6 }
]);
const serviceFeedback = ref([
  { id: "feedback-001", messageId: "coupon-answer", question: "新人优惠券怎么领？", result: "有帮助", reason: "规则清晰", status: "已归档", time: "今天 10:32" },
  { id: "feedback-002", messageId: "seed-002", question: "签到积分可以提现吗？", result: "没帮助", reason: "缺少兑换范围说明", status: "待处理", time: "今天 09:48" },
  { id: "feedback-003", messageId: "seed-003", question: "拼团团长奖励何时到账？", result: "没帮助", reason: "检索到的规则不完整", status: "已转知识缺口", time: "昨天 18:20" }
]);
const serviceTickets = ref([
  { id: "CS20260908001", user: "林晓敏", question: "新人券已经领到，但支付时提示不可用。", category: "优惠券使用", status: "待分配", time: "今天 10:41", source: "RAG 转人工", context: "用户已注册 2 天，持有新人首单券 ¥28，订单金额 ¥126。", retrieval: "命中新人优惠券活动规则，但未覆盖支付页不可用异常。", handler: "—" },
  { id: "CS20260908002", user: "陈子墨", question: "拼团失败后退款一直没有到账。", category: "拼团售后", status: "处理中", time: "今天 10:16", source: "用户主动转人工", context: "订单 GP20260907082，团购活动已结束，支付金额 ¥79。", retrieval: "命中拼团退款规则，用户仍需查询具体退款流水。", handler: "王雯" },
  { id: "CS20260907018", user: "周元昊", question: "会员券和限时券可以同时使用吗？", category: "活动规则", status: "待补充知识", time: "昨天 18:20", source: "低分回答转入", context: "银卡会员，当前有一张限时满减券和一张会员复购券。", retrieval: "知识库中缺少券叠加规则的统一说明。", handler: "陈嘉" }
]);
const selectedTicketId = ref("CS20260908001");
const growthMenuOpen = ref(true);
const serviceMenuOpen = ref(true);

const portal = ref("admin");
const pageKey = ref("dashboard");
const mobileNavOpen = ref(false);
const notice = ref("");
const query = ref("");
const selectedRange = ref("近 7 天");
const selectedChannel = ref("全部活动");
const funnelSource = ref("全部广告来源");
const refreshing = ref(false);
const handledAlerts = ref([]);
const selectedSegmentId = ref("new-users");
const rewardStatus = ref("全部状态");
const ruleTestResult = ref("");
const authMode = ref("");
const authForm = ref({ phone: "", password: "", confirmPassword: "", remember: true, agree: true });
const authError = ref("");
const authSubmitting = ref(false);
const currentNav = computed(() => portal.value === "admin" ? adminPages : clientPages);
const page = computed(() => currentNav.value.find((item) => item.key === pageKey.value) ?? currentNav.value[0]);
const adminGrowthNav = computed(() => adminPages.filter((item) => !["service", "knowledge", "feedback", "tickets"].includes(item.key)));
const adminServiceNav = computed(() => adminPages.filter((item) => ["service", "knowledge", "feedback", "tickets"].includes(item.key)));
const adminNavGroups = computed(() => [
  { key: "growth", label: "用户增长功能", items: adminGrowthNav.value, open: growthMenuOpen.value },
  { key: "service", label: "客服功能", items: adminServiceNav.value, open: serviceMenuOpen.value }
]);
const isOverview = computed(() => portal.value === "admin" && pageKey.value === "dashboard");
const isServicePage = computed(() => pageKey.value === "service");
const isKnowledgePage = computed(() => portal.value === "admin" && pageKey.value === "knowledge");
const isFeedbackPage = computed(() => portal.value === "admin" && pageKey.value === "feedback");
const isTicketsPage = computed(() => portal.value === "admin" && pageKey.value === "tickets");
const selectedTicket = computed(() => serviceTickets.value.find((item) => item.id === selectedTicketId.value) ?? serviceTickets.value[0]);
const isGrowthTool = computed(() => portal.value === "admin" && ["funnel", "segments", "automation", "rules", "rewards"].includes(pageKey.value));
const selectedSegment = computed(() => segments.find((item) => item.id === selectedSegmentId.value) ?? segments[0]);
const visibleRewards = computed(() => rewardRecords.value.filter((item) => rewardStatus.value === "全部状态" || item.status === rewardStatus.value));
const pageMetrics = computed(() => {
  if (portal.value === "client") return clientMetrics;
  return [["活动参与", "1,286", "较上期 +8.4%", "真实用户占比 96.2%"], ["待处理", "8", "需要关注", "可立即处理"], ["奖励发放", "2,342", "发放成功率 99.4%", "今日已发放"], ["转化提升", "12.6%", "较上周 +2.3%", "数据已同步"]].map(([label, value, trend, note]) => ({ label: `${page.value.label}${label}`, value, trend, note }));
});
const visibleCampaigns = computed(() => {
  const keyword = query.value.trim().toLowerCase();
  return campaigns.filter((item) => (selectedChannel.value === "全部活动" || item.type === selectedChannel.value) && (!keyword || `${item.name}${item.type}`.toLowerCase().includes(keyword)));
});
const rows = computed(() => {
  const itemMap = {
    advertising: ["广告素材与落地页", "广告来源归因", "注册成本监控", "异常流量排查"],
    registration: ["落地页注册转化", "广告来源注册", "注册失败分析", "新用户激活"],
    funnel: ["广告来源漏斗", "注册流失节点", "领券到首单转化", "复购回流分析"],
    segments: ["新注册未领券", "领券未下单", "高活跃邀请者", "沉默会员"],
    automation: ["注册后新人券引导", "领券未下单催化", "沉默会员召回", "异常奖励拦截"],
    rules: ["新人券发放规则", "签到积分规则", "邀请双向奖励规则", "秒杀限购规则"],
    rewards: ["新人券发放流水", "签到积分流水", "邀请奖励待审核", "秒杀成长值待审核"],
    coupons: ["新人券发放规则", "满减券库存", "新人券核销任务", "即将过期提醒"],
    checkin: ["连续签到奖励", "积分任务配置", "本周签到榜", "异常补签记录"],
    referral: ["邀请奖励活动", "好友助力任务", "邀请码关系", "异常邀请审核"],
    groupbuy: ["周末拼团活动", "待成团订单", "团长奖励发放", "拼团库存预警"],
    seckill: ["晚间秒杀场次", "秒杀库存锁定", "限购订单审核", "异常订单复核"],
    "limited-coupon": ["3 小时限时券", "倒计时提醒任务", "券库存预警", "限时券核销"],
    membership: ["会员等级规则", "成长值发放", "复购券任务", "沉睡会员召回"],
    risk: ["同设备多账号", "异常领券行为", "异常签到频次", "秒杀订单复核"],
    newbie: ["新人券待领取", "首单优惠提醒", "新人任务进度", "券即将过期"],
    invite: ["邀请好友奖励", "好友注册进度", "助力任务", "邀请奖励记录"],
    deals: ["正在拼团", "今晚秒杀", "限时优惠券", "活动提醒"],
    member: ["成长值进度", "本月复购券", "会员专属权益", "等级升级任务"],
    "my-coupons": ["新人首单券", "满减优惠券", "即将过期券", "已使用优惠券"]
  };
  const names = itemMap[pageKey.value] ?? ["增长任务", "活动配置", "奖励发放", "数据监控"];
  return names.map((name, index) => ({ name, owner: portal.value === "admin" ? (index % 2 ? "王雯" : "陈嘉") : "我", time: `${index + 1} 小时前`, status: index === 2 ? "需关注" : "进行中" }));
});
const actions = computed(() => {
  if (portal.value === "client") return ["领取奖励", "查看规则", "参与活动"];
  const items = { advertising: ["新建广告投放", "配置落地页", "查看来源归因"], registration: ["查看转化", "配置注册流程", "导出注册数据"], funnel: ["切换漏斗口径", "查看流失用户", "导出漏斗数据"], segments: ["新建用户分群", "编辑标签规则", "发起定向活动"], automation: ["新建自动化", "查看执行日志", "暂停全部流程"], rules: ["新建营销规则", "模拟规则命中", "查看规则版本"], rewards: ["查看发奖流水", "审核高风险奖励", "补发奖励"], coupons: ["创建新人券", "调整库存", "查看核销"], checkin: ["配置签到", "新增积分任务", "查看排行榜"], referral: ["创建邀请活动", "查看邀请关系", "审核异常邀请"], groupbuy: ["创建拼团", "查看待成团", "配置团长奖励"], seckill: ["创建秒杀场次", "调整库存", "查看异常订单"], "limited-coupon": ["创建限时券", "配置提醒", "查看核销"], membership: ["配置会员等级", "创建复购券", "发起召回"], risk: ["查看风险名单", "调整拦截规则", "处理异常订单"] };
  return items[pageKey.value] ?? ["查看增长链路", "配置活动", "查看风控"];
});

function navigate(nextPortal, nextPage, replace = false) {
  portal.value = nextPortal;
  pageKey.value = nextPage;
  if (nextPortal === "admin") {
    if (["service", "knowledge", "feedback", "tickets"].includes(nextPage)) serviceMenuOpen.value = true;
    else growthMenuOpen.value = true;
  }
  mobileNavOpen.value = false;
  const path = `/${nextPortal}/${nextPage}`;
  if (window.location.pathname !== path) window.history[replace ? "replaceState" : "pushState"]({}, "", path);
}
function toggleNavGroup(groupKey) {
  if (groupKey === "growth") growthMenuOpen.value = !growthMenuOpen.value;
  if (groupKey === "service") serviceMenuOpen.value = !serviceMenuOpen.value;
}
function readLocation() {
  const authPath = window.location.pathname;
  if (authPath === "/login" || authPath === "/register") {
    authMode.value = authPath === "/register" ? "register" : "login";
    authError.value = "";
    mobileNavOpen.value = false;
    return;
  }
  authMode.value = "";
  const [, requestedPortal, requestedPage] = window.location.pathname.split("/");
  const nextPortal = requestedPortal === "client" ? "client" : "admin";
  const pages = nextPortal === "client" ? clientPages : adminPages;
  navigate(nextPortal, pages.some((item) => item.key === requestedPage) ? requestedPage : pages[0].key, true);
}
function openAuth(mode) {
  authMode.value = mode;
  authError.value = "";
  authSubmitting.value = false;
  const path = mode === "register" ? "/register" : "/login";
  if (window.location.pathname !== path) window.history.pushState({}, "", path);
}
function submitAuth() {
  const phone = authForm.value.phone.trim();
  if (!/^1\d{10}$/.test(phone)) {
    authError.value = "请输入 11 位中国大陆手机号，例如 13800138000。";
    return;
  }
  if (authForm.value.password.length < 6) {
    authError.value = "密码至少需要 6 位字符。";
    return;
  }
  if (authMode.value === "register" && authForm.value.password !== authForm.value.confirmPassword) {
    authError.value = "两次输入的密码不一致，请重新确认。";
    return;
  }
  if (authMode.value === "register" && !authForm.value.agree) {
    authError.value = "请先阅读并同意服务协议与隐私说明。";
    return;
  }
  authError.value = "";
  authSubmitting.value = true;
  const submittedMode = authMode.value;
  window.setTimeout(() => {
    authSubmitting.value = false;
    authMode.value = "";
    navigate("admin", "dashboard", true);
    flash(submittedMode === "register" ? "注册成功，欢迎进入增长运营台" : "登录成功，已进入增长运营台");
  }, 620);
}
function flash(message) { notice.value = message; window.setTimeout(() => { notice.value = ""; }, 3000); }
function refresh() { refreshing.value = true; window.setTimeout(() => { refreshing.value = false; flash("数据已更新至 10:36"); }, 560); }
function resolveAlert(id) { handledAlerts.value = [...handledAlerts.value, id]; flash("预警已标记为已处理"); }
function toggleFlow(id) {
  automationFlows.value = automationFlows.value.map((item) => item.id === id ? { ...item, enabled: !item.enabled } : item);
  const flow = automationFlows.value.find((item) => item.id === id);
  flash(`${flow.name}已${flow.enabled ? "启用" : "暂停"}`);
}
function toggleRule(id) {
  marketingRules.value = marketingRules.value.map((item) => item.id === id ? { ...item, enabled: !item.enabled } : item);
  const rule = marketingRules.value.find((item) => item.id === id);
  flash(`${rule.name}已${rule.enabled ? "启用" : "停用"}`);
}
function testRule(rule) {
  ruleTestResult.value = `模拟命中「${rule.name}」：用户满足“${rule.condition}”，将执行“${rule.outcome}”。`;
}
function reviewReward(id) {
  rewardRecords.value = rewardRecords.value.map((item) => item.id === id ? { ...item, status: "已发放", risk: "人工复核通过" } : item);
  flash("奖励已审核通过并进入发放队列");
}
function askService(question) {
  customerQuestion.value = question;
  sendServiceMessage();
}
function getServiceAnswer(question) {
  const content = question.toLowerCase();
  if (content.includes("签到") || content.includes("积分")) return { text: "签到积分不能直接提现，但可以用于兑换优惠券和会员权益。每日签到可获得 10–30 积分，连续签到会提高奖励；如遇签到异常，可在当天通过任务页提交补签申请。", sources: ["签到与积分说明", "积分任务规则"] };
  if (content.includes("拼团") || content.includes("秒杀") || content.includes("退款")) return { text: "拼团在活动结束前未成团时，已支付金额会按原支付路径退回；秒杀商品以订单支付成功为准，库存锁定和限购规则会同时校验。", sources: ["拼团秒杀常见问题", "秒杀限购规则"] };
  if (content.includes("会员") || content.includes("等级")) return { text: "会员等级由成长值决定。完成首单、连续签到、参与活动和复购都可以获得成长值；升级后会自动获得对应的专属券和活动权益。", sources: ["会员权益与召回策略", "会员等级规则"] };
  return { text: "我已根据当前知识库检索到相关规则。你可以补充活动名称、订单状态或遇到的具体问题，我会继续为你定位处理方式。", sources: ["新人优惠券活动规则", "拼团秒杀常见问题"] };
}
function sendServiceMessage() {
  const question = customerQuestion.value.trim();
  if (!question || customerSending.value) return;
  customerMessages.value.push({ id: `user-${Date.now()}`, role: "user", text: question, time: "现在" });
  customerQuestion.value = "";
  customerSending.value = true;
  window.setTimeout(() => {
    const answer = getServiceAnswer(question);
    customerMessages.value.push({ id: `assistant-${Date.now()}`, role: "assistant", text: answer.text, time: "现在", sources: answer.sources });
    customerSending.value = false;
  }, 540);
}
function rateServiceAnswer(message, result) {
  const messageIndex = customerMessages.value.findIndex((item) => item.id === message.id);
  const question = [...customerMessages.value.slice(0, messageIndex)].reverse().find((item) => item.role === "user")?.text ?? "未关联问题";
  customerMessages.value = customerMessages.value.map((item) => item.id === message.id ? { ...item, feedback: result } : item);
  const existing = serviceFeedback.value.find((item) => item.messageId === message.id);
  const feedback = { id: existing?.id ?? `feedback-${Date.now()}`, messageId: message.id, question, result, reason: result === "有帮助" ? "用户确认已解决" : "用户认为回答未解决问题", status: result === "有帮助" ? "已归档" : "待处理", time: "刚刚" };
  serviceFeedback.value = existing ? serviceFeedback.value.map((item) => item.messageId === message.id ? feedback : item) : [feedback, ...serviceFeedback.value];
  if (result === "没帮助") {
    const existingGap = knowledgeGaps.value.find((item) => item.question === question);
    if (!existingGap) knowledgeGaps.value = [{ id: `gap-${Date.now()}`, question, detail: "来自用户低分评价，等待运营补充标准答案。", score: "低", status: "待补充", count: 1 }, ...knowledgeGaps.value];
    flash("已记录反馈，并进入知识缺口待处理队列");
    return;
  }
  flash("感谢反馈，这条回答已标记为有效");
}
function createHumanTicket() {
  const question = [...customerMessages.value].reverse().find((item) => item.role === "user")?.text ?? "需要人工协助处理活动规则问题";
  const ticket = { id: `CS${new Date().toISOString().slice(0, 10).replaceAll("-", "")}${String(serviceTickets.value.length + 1).padStart(3, "0")}`, user: "宋诚", question, category: "智能客服转人工", status: "待分配", time: "刚刚", source: "用户端 RAG 转人工", context: "已携带当前对话上下文、用户基础信息和最近活动参与记录。", retrieval: "RAG 已给出引用回答，用户选择继续由人工客服处理。", handler: "—" };
  serviceTickets.value = [ticket, ...serviceTickets.value];
  selectedTicketId.value = ticket.id;
  flash(`已创建人工工单 ${ticket.id}，客服将尽快处理`);
}
function convertFeedbackToGap(feedback) {
  const hasGap = knowledgeGaps.value.some((item) => item.question === feedback.question);
  if (!hasGap) knowledgeGaps.value = [{ id: `gap-${Date.now()}`, question: feedback.question, detail: `来自「${feedback.reason}」的低分评价，等待补齐规则。`, score: "低", status: "待补充", count: 1 }, ...knowledgeGaps.value];
  serviceFeedback.value = serviceFeedback.value.map((item) => item.id === feedback.id ? { ...item, status: "已转知识缺口" } : item);
  flash("已转入知识缺口，并可继续上传规则文档");
}
function assignTicket(ticket) {
  serviceTickets.value = serviceTickets.value.map((item) => item.id === ticket.id ? { ...item, status: "处理中", handler: "宋诚" } : item);
  flash(`工单 ${ticket.id} 已分配给你`);
}
function resolveTicket(ticket) {
  serviceTickets.value = serviceTickets.value.map((item) => item.id === ticket.id ? { ...item, status: "已解决", handler: item.handler === "—" ? "宋诚" : item.handler } : item);
  flash(`工单 ${ticket.id} 已标记为解决`);
}
function openKnowledgeUpload() {
  knowledgeUpload.value?.click();
}
function uploadKnowledgeDocument(event) {
  const [file] = event.target.files ?? [];
  if (!file) return;
  const suffix = file.name.includes(".") ? file.name.split(".").pop().toUpperCase() : "FILE";
  knowledgeDocuments.value = [{ id: `doc-${Date.now()}`, name: file.name, format: suffix, chunks: "正在解析", updatedAt: "刚刚上传", status: "处理中" }, ...knowledgeDocuments.value];
  event.target.value = "";
  flash(`已加入「${file.name}」，文档解析完成后将进入知识库`);
}
function removeKnowledgeDocument(id) {
  knowledgeDocuments.value = knowledgeDocuments.value.filter((item) => item.id !== id);
  flash("文档已从知识库移除");
}

onMounted(() => { readLocation(); window.addEventListener("popstate", readLocation); });
onBeforeUnmount(() => window.removeEventListener("popstate", readLocation));
</script>

<template>
  <main v-if="authMode" class="auth-shell">
    <header class="auth-intro">
      <button class="auth-brand" type="button" @click="openAuth('login')"><span class="brand-mark">A</span><span>AdGrowth</span></button>
    </header>

    <section class="auth-panel" aria-labelledby="auth-title">
      <div class="auth-card">
        <div class="auth-card-head"><h2 id="auth-title">{{ authMode === 'login' ? '登录' : '注册' }}</h2></div>

        <form class="auth-form" @submit.prevent="submitAuth" novalidate>
          <label class="auth-field"><span>手机号</span><input v-model="authForm.phone" type="tel" inputmode="numeric" autocomplete="tel" placeholder="请输入手机号" maxlength="11" required /></label>
          <label class="auth-field"><span>密码</span><input v-model="authForm.password" type="password" :autocomplete="authMode === 'register' ? 'new-password' : 'current-password'" placeholder="至少 6 位字符" minlength="6" required /></label>
          <label v-if="authMode === 'register'" class="auth-field"><span>确认密码</span><input v-model="authForm.confirmPassword" type="password" autocomplete="new-password" placeholder="再次输入密码" minlength="6" required /></label>

          <label v-if="authMode === 'login'" class="auth-check"><input v-model="authForm.remember" type="checkbox" /><span>记住登录状态</span></label>
          <label v-else class="auth-check"><input v-model="authForm.agree" type="checkbox" /><span>我已阅读并同意服务协议与隐私说明</span></label>

          <p v-if="authError" class="auth-error" role="alert">{{ authError }}</p>
          <button class="auth-submit" type="submit" :disabled="authSubmitting">{{ authSubmitting ? '提交中…' : authMode === 'login' ? '登录' : '注册' }} <span v-if="!authSubmitting">→</span></button>
        </form>

        <p class="auth-switch">{{ authMode === 'login' ? '还没有账号？' : '已经有账号？' }}<button type="button" @click="openAuth(authMode === 'login' ? 'register' : 'login')">{{ authMode === 'login' ? '注册账户' : '去登录' }}</button></p>
      </div>
    </section>
  </main>

  <div v-else class="app-shell" :class="`portal-${portal}`">
    <aside class="sidebar" aria-label="主导航">
      <button class="brand brand-button" type="button" @click="navigate(portal, portal === 'admin' ? 'dashboard' : 'home')"><span class="brand-mark">A</span><span>AdGrowth</span></button>
      <div class="portal-switcher">
        <button type="button" :class="{ active: portal === 'admin' }" @click="navigate('admin', 'dashboard')">管理端</button>
        <button type="button" :class="{ active: portal === 'client' }" @click="navigate('client', 'home')">用户端</button>
      </div>
      <p class="workspace-label">{{ portal === "admin" ? "工作台" : "功能导航" }}</p>
      <nav v-if="portal === 'admin'" class="nav-menu">
        <section v-for="group in adminNavGroups" :key="group.key" class="nav-group" :class="{ active: group.items.some(item => item.key === pageKey) }">
          <button class="nav-group-toggle" type="button" :aria-expanded="group.open" @click="toggleNavGroup(group.key)"><span>{{ group.label }}</span><small>{{ group.items.length }}</small><b :class="{ open: group.open }">⌄</b></button>
          <div v-show="group.open" class="nav-group-items">
            <button v-for="item in group.items" :key="item.key" class="nav-item" :class="{ active: pageKey === item.key }" type="button" @click="navigate(portal, item.key)">
              <span class="nav-icon">{{ item.icon }}</span><span>{{ item.label }}</span>
              <span v-if="item.key === 'risk'" class="nav-badge">{{ riskAlerts.length - handledAlerts.length }}</span>
            </button>
          </div>
        </section>
      </nav>
      <nav v-else>
        <button v-for="item in currentNav" :key="item.key" class="nav-item" :class="{ active: pageKey === item.key }" type="button" @click="navigate(portal, item.key)">
          <span class="nav-icon">{{ item.icon }}</span><span>{{ item.label }}</span>
          <span v-if="item.key === 'my-coupons'" class="nav-badge neutral">3</span>
        </button>
      </nav>
      <div class="sidebar-footer"><div class="profile-avatar">SC</div><div class="profile-copy"><strong>宋诚</strong><span>{{ portal === "admin" ? "增长管理员" : "增长用户" }}</span></div><button class="more-button" type="button" @click="openAuth('login')">···</button></div>
    </aside>

    <main class="content">
      <header class="top-header">
        <button class="mobile-menu" type="button" aria-label="打开主导航" @click="mobileNavOpen = true">☰</button>
        <div class="breadcrumb"><span>{{ portal === "admin" ? "管理端" : "用户端" }}</span><b>/</b><strong>{{ page.label }}</strong></div>
        <div class="header-actions"><button class="portal-link" type="button" @click="navigate(portal === 'admin' ? 'client' : 'admin', portal === 'admin' ? 'home' : 'dashboard')">{{ portal === "admin" ? "用户端" : "管理端" }} <span>→</span></button><button class="icon-button" type="button" @click="flash('没有新的通知')">◌<span class="notification-dot"></span></button><button class="avatar-button" type="button" @click="navigate('client', 'member')">SC</button></div>
      </header>

      <template v-if="isOverview">
        <section class="page-heading"><div><h1>增长总览</h1></div><div class="toolbar"><label class="select-wrap"><select v-model="selectedRange"><option>今日</option><option>近 7 天</option><option>近 30 天</option></select></label><label class="select-wrap"><select v-model="selectedChannel"><option>全部活动</option><option>新人优惠券</option><option>签到积分</option><option>邀请裂变</option><option>拼团秒杀</option><option>限时券</option></select></label><button class="refresh-button" :class="{ loading: refreshing }" type="button" @click="refresh"><span>↻</span>{{ refreshing ? "更新中" : "刷新数据" }}</button></div></section>
        <section class="metric-grid"><article v-for="(metric, index) in overviewMetrics" :key="metric.label" class="metric reveal" :style="{ '--reveal-delay': `${index * 38}ms` }"><p>{{ metric.label }}</p><strong>{{ metric.value }}</strong><div><span class="metric-trend up">{{ metric.trend }}</span><span>{{ metric.note }}</span></div></article></section>
        <section class="analysis-grid">
          <article class="panel trend-panel reveal" style="--reveal-delay: 130ms"><div class="panel-heading"><div><h2>注册与首单趋势</h2></div><button class="subtle-button" type="button" @click="navigate('admin', 'registration')">查看注册转化 <span>→</span></button></div><div class="chart-legend"><span><i class="legend-dot spend"></i>完成注册</span><span><i class="legend-dot conversion"></i>完成首单</span></div><div class="line-chart"><div class="chart-y-axis"><span>240</span><span>180</span><span>120</span><span>60</span></div><div class="chart-area"><div class="chart-grid-lines"></div><svg viewBox="0 0 278 102" preserveAspectRatio="none"><defs><linearGradient id="spendFill" x1="0" x2="0" y1="0" y2="1"><stop offset="0%" stop-color="#4e8aab" stop-opacity=".28" /><stop offset="100%" stop-color="#4e8aab" stop-opacity="0" /></linearGradient></defs><path d="M0 95 C18 80 25 84 40 69 S64 78 80 57 S108 60 126 44 S150 53 168 31 S198 38 220 17 S255 19 278 8 L278 102 L0 102 Z" fill="url(#spendFill)" /><path d="M0 95 C18 80 25 84 40 69 S64 78 80 57 S108 60 126 44 S150 53 168 31 S198 38 220 17 S255 19 278 8" fill="none" stroke="#2d7095" stroke-width="2.2" /><path d="M0 87 C20 78 30 83 40 76 S65 81 80 68 S105 71 126 62 S146 68 168 53 S194 56 220 41 S252 46 278 27" fill="none" stroke="#28906b" stroke-width="1.8" stroke-dasharray="3 3" /></svg><div class="chart-x-axis"><span>09/01</span><span>09/02</span><span>09/03</span><span>09/04</span><span>09/05</span><span>09/06</span><span>09/07</span></div></div></div></article>
          <article class="panel channel-panel reveal" style="--reveal-delay: 175ms"><div class="panel-heading"><div><h2>广告到复购的增长路径</h2></div><button class="subtle-button" type="button" @click="navigate('admin', 'risk')">查看风控 <span>→</span></button></div><div class="growth-flow"><button v-for="step in growthFlow" :key="step.index" class="growth-flow-step" type="button" @click="navigate('admin', step.target)"><span class="growth-flow-index">{{ step.index }}</span><span class="growth-flow-copy"><strong>{{ step.title }}</strong></span><span class="growth-flow-arrow">→</span></button></div></article>
        </section>
        <section class="bottom-grid"><article class="panel campaign-panel reveal" style="--reveal-delay: 215ms"><div class="panel-heading campaign-heading"><div><h2>增长活动执行</h2></div><label class="search"><span>⌕</span><input v-model="query" type="search" placeholder="搜索活动或类型" /></label></div><div class="table-scroll"><table><thead><tr><th>活动</th><th>活动类型</th><th>参与用户</th><th>首单转化</th><th>激励成本</th><th>转化率</th><th>状态</th></tr></thead><tbody><tr v-for="campaign in visibleCampaigns" :key="campaign.name"><td><strong>{{ campaign.name }}</strong></td><td>{{ campaign.type }}</td><td>{{ campaign.participants }}</td><td>{{ campaign.firstOrders }}</td><td>{{ campaign.rewardCost }}</td><td class="roi-value">{{ campaign.rate }}</td><td><span class="status" :class="{ warning: campaign.status === '需关注' }"><i></i>{{ campaign.status }}</span></td></tr><tr v-if="!visibleCampaigns.length"><td class="empty-cell" colspan="7">没有匹配的增长活动，请清除筛选条件后重试。</td></tr></tbody></table></div><button class="table-footer" type="button" @click="navigate('admin', 'limited-coupon')">查看限时活动 <span>→</span></button></article>
          <article class="panel alert-panel reveal" style="--reveal-delay: 255ms"><div class="panel-heading"><div><h2>反作弊预警</h2></div><span class="alert-count">{{ riskAlerts.length - handledAlerts.length }}</span></div><div v-for="alert in riskAlerts" v-show="!handledAlerts.includes(alert.id)" :key="alert.id" class="alert-item"><span class="alert-symbol" :class="alert.tone">!</span><div><strong>{{ alert.title }}</strong><p>{{ alert.description }}</p><button type="button" @click="resolveAlert(alert.id)">标记已处理</button></div></div><div v-if="handledAlerts.length === riskAlerts.length" class="alerts-empty"><span>✓</span><p>当前没有待处理风控预警。</p></div></article></section>
      </template>

      <template v-else-if="isServicePage">
        <template v-if="portal === 'admin'">
          <section class="page-heading"><div><h1>RAG 智能客服</h1></div><button class="refresh-button" type="button" @click="navigate('admin', 'knowledge')"><span>↑</span>管理知识库</button></section>
          <section class="metric-grid"><article v-for="(metric, index) in [{ label: '今日咨询', value: '186', trend: '较昨日 +12.4%', note: '用户咨询与转人工' }, { label: 'RAG 命中率', value: '94.2%', trend: '较昨日 +1.8%', note: '回答引用有效知识' }, { label: '一次解决率', value: '86.7%', trend: '较昨日 +3.1%', note: '无需人工介入' }, { label: '待补齐问题', value: '7', trend: '需要处理', note: '低置信度问题' } ]" :key="metric.label" class="metric reveal" :style="{ '--reveal-delay': `${index * 35}ms` }"><p>{{ metric.label }}</p><strong>{{ metric.value }}</strong><div><span class="metric-trend up">{{ metric.trend }}</span><span>{{ metric.note }}</span></div></article></section>
          <section class="service-admin-grid">
            <article class="panel service-panel reveal"><div class="panel-heading"><div><h2>RAG 流程</h2></div><span class="status"><i></i>正常</span></div><div class="rag-pipeline"><div><b>01</b><span><strong>提问</strong><small>新人优惠券</small></span></div><div><b>02</b><span><strong>识别</strong><small>新人权益</small></span></div><div><b>03</b><span><strong>检索</strong><small>2 个片段</small></span></div><div><b>04</b><span><strong>回答</strong><small>引用来源</small></span></div></div><div class="retrieval-card"><p>命中文档</p><strong>新人优惠券活动规则.pdf</strong><span>满 ¥99 减 ¥28 · 有效期 7 天</span><em>0.92</em></div></article>
            <article class="panel service-panel reveal" style="--reveal-delay: 60ms"><div class="panel-heading"><div><h2>知识缺口</h2></div><button class="subtle-button" type="button" @click="navigate('admin', 'feedback')">查看闭环 <span>→</span></button></div><div class="service-gap-list"><div v-for="gap in knowledgeGaps.slice(0, 3)" :key="gap.id"><span class="gap-score" :class="{ warning: gap.score === '中' }">{{ gap.score }}</span><p><strong>{{ gap.question }}</strong><small>{{ gap.detail }}</small></p></div></div></article>
          </section>
          <section class="panel service-panel service-question-panel reveal" style="--reveal-delay: 110ms"><div class="panel-heading"><div><h2>问题命中率</h2></div><button class="subtle-button" type="button" @click="navigate('client', 'service')">用户端试问 <span>→</span></button></div><div class="question-health"><div><strong>新人优惠券怎么领？</strong><span>46 次 · 证据完整</span><b>96%</b></div><div><strong>拼团没有成团怎么办？</strong><span>33 次 · 证据完整</span><b>93%</b></div><div><strong>签到积分可以提现吗？</strong><span>28 次 · 待补充</span><b class="warning-text">78%</b></div></div></section>
        </template>

        <template v-else>
          <section class="page-heading client-service-heading"><div><h1>智能客服</h1></div><button class="portal-link client-human-button" type="button" @click="createHumanTicket">转人工客服 <span>→</span></button></section>
          <section class="client-service-grid">
            <article class="panel chat-panel reveal"><header class="chat-header"><div><span class="chat-assistant-mark">◔</span><span><strong>AdGrowth 智能客服</strong><small>基于业务知识库回答</small></span></div><span class="status"><i></i>在线</span></header><div class="chat-messages" aria-live="polite"><article v-for="message in customerMessages" :key="message.id" class="chat-message" :class="message.role"><div class="chat-bubble"><p>{{ message.text }}</p><div v-if="message.sources?.length" class="chat-sources"><span v-for="source in message.sources" :key="source">来源：{{ source }}</span></div><div v-if="message.role === 'assistant' && message.sources?.length" class="chat-feedback"><span>这条回答有帮助吗？</span><button type="button" :class="{ active: message.feedback === '有帮助' }" @click="rateServiceAnswer(message, '有帮助')">有帮助</button><button type="button" :class="{ active: message.feedback === '没帮助' }" @click="rateServiceAnswer(message, '没帮助')">没帮助</button></div><small>{{ message.time }}</small></div></article><article v-if="customerSending" class="chat-message assistant"><div class="chat-bubble typing"><i></i><i></i><i></i></div></article></div><form class="chat-composer" @submit.prevent="sendServiceMessage"><textarea v-model="customerQuestion" rows="2" placeholder="输入你的问题，例如：拼团失败后会退款吗？" @keydown.enter.exact.prevent="sendServiceMessage"></textarea><button class="refresh-button" type="submit" :disabled="customerSending || !customerQuestion.trim()">发送 <span>↑</span></button></form></article>
            <aside class="panel service-side reveal" style="--reveal-delay: 60ms"><div class="panel-heading"><div><h2>试着这样问</h2></div></div><button v-for="item in serviceSuggestions" :key="item" class="service-suggestion" type="button" @click="askService(item)"><span>{{ item }}</span><b>→</b></button></aside>
          </section>
        </template>
      </template>

      <template v-else-if="isKnowledgePage">
        <section class="page-heading"><div><h1>知识库管理</h1></div><button class="refresh-button" type="button" @click="openKnowledgeUpload"><span>↑</span>上传文档</button></section>
        <input ref="knowledgeUpload" class="sr-only" type="file" accept=".pdf,.doc,.docx,.md,.txt" @change="uploadKnowledgeDocument" />
        <section class="knowledge-summary"><article class="panel knowledge-upload reveal"><div><h2>业务文档</h2><p>PDF、Word、Markdown、TXT</p></div><button type="button" class="knowledge-upload-button" @click="openKnowledgeUpload"><span>↑</span>上传</button></article><article class="panel knowledge-guideline reveal" style="--reveal-delay: 55ms"><h2>文档规则</h2><ul><li>活动与生效时间</li><li>资格与限制</li><li>异常处理</li></ul></article></section>
        <section class="panel service-panel knowledge-table reveal" style="--reveal-delay: 95ms"><div class="panel-heading"><div><h2>{{ knowledgeDocuments.length }} 份知识资产</h2></div><button class="subtle-button" type="button" @click="flash('知识库索引已刷新')">刷新索引 <span>↻</span></button></div><div class="table-scroll"><table><thead><tr><th>文档名称</th><th>格式</th><th>知识片段</th><th>更新时间</th><th>状态</th><th>操作</th></tr></thead><tbody><tr v-for="doc in knowledgeDocuments" :key="doc.id"><td><strong>{{ doc.name }}</strong></td><td>{{ doc.format }}</td><td>{{ doc.chunks }}</td><td>{{ doc.updatedAt }}</td><td><span class="status" :class="{ warning: doc.status !== '已发布' }"><i></i>{{ doc.status }}</span></td><td><button class="subtle-button" type="button" @click="removeKnowledgeDocument(doc.id)">移除</button></td></tr></tbody></table></div></section>
      </template>


      <template v-else-if="isFeedbackPage">
        <section class="page-heading"><div><h1>服务评价</h1></div><button class="refresh-button" type="button" @click="navigate('admin', 'knowledge')"><span>↑</span>补充知识文档</button></section>
        <section class="metric-grid"><article v-for="(metric, index) in [{ label: '今日有效评价', value: serviceFeedback.length + 34, trend: '评价率 28.6%', note: '用户主动反馈' }, { label: '有帮助占比', value: '88.4%', trend: '较昨日 +2.7%', note: '回答有效性' }, { label: '待处理低分', value: serviceFeedback.filter(item => item.status === '待处理').length, trend: '需要运营判断', note: '可转知识缺口' }, { label: '已补充缺口', value: '13', trend: '本周已完成', note: '重新发布索引' } ]" :key="metric.label" class="metric reveal" :style="{ '--reveal-delay': `${index * 35}ms` }"><p>{{ metric.label }}</p><strong>{{ metric.value }}</strong><div><span class="metric-trend up">{{ metric.trend }}</span><span>{{ metric.note }}</span></div></article></section>
        <section class="feedback-grid">
          <article class="panel feedback-panel reveal"><div class="panel-heading"><div><h2>需要处理的用户反馈</h2></div><span class="status warning"><i></i>{{ serviceFeedback.filter(item => item.status === '待处理').length }} 待处理</span></div><div class="feedback-list"><article v-for="feedback in serviceFeedback" :key="feedback.id" class="feedback-row"><div class="feedback-result" :class="{ negative: feedback.result === '没帮助' }">{{ feedback.result === '有帮助' ? '✓' : '!' }}</div><div class="feedback-copy"><strong>{{ feedback.question }}</strong><p>{{ feedback.reason }}</p><small>{{ feedback.time }} · {{ feedback.result }}</small></div><div class="feedback-actions"><span class="status" :class="{ warning: feedback.status !== '已归档' }"><i></i>{{ feedback.status }}</span><button v-if="feedback.result === '没帮助' && feedback.status === '待处理'" class="subtle-button" type="button" @click="convertFeedbackToGap(feedback)">转知识缺口</button></div></article></div></article>
          <aside class="panel feedback-panel feedback-gap-panel reveal" style="--reveal-delay: 60ms"><div class="panel-heading"><div><h2>等待补齐的规则</h2></div><button class="subtle-button" type="button" @click="navigate('admin', 'knowledge')">上传文档 <span>→</span></button></div><div class="feedback-gap-list"><div v-for="gap in knowledgeGaps" :key="gap.id"><span class="gap-score" :class="{ warning: gap.score === '中' }">{{ gap.score }}</span><p><strong>{{ gap.question }}</strong><small>{{ gap.count }} 条相关咨询 · {{ gap.status }}</small></p></div></div></aside>
        </section>
      </template>

      <template v-else-if="isTicketsPage">
        <section class="page-heading"><div><h1>人工工单</h1></div><button class="refresh-button" type="button" @click="navigate('client', 'service')"><span>◔</span>从用户端创建工单</button></section>
        <section class="metric-grid"><article v-for="(metric, index) in [{ label: '待分配工单', value: serviceTickets.filter(item => item.status === '待分配').length, trend: '优先处理', note: 'RAG 转人工' }, { label: '处理中', value: serviceTickets.filter(item => item.status === '处理中').length, trend: '平均响应 4 分钟', note: '客服已接入' }, { label: '一次解决率', value: '82.3%', trend: '较昨日 +1.5%', note: '人工处理结果' }, { label: '待补充知识', value: serviceTickets.filter(item => item.status === '待补充知识').length, trend: '需要运营补齐', note: '规则不完整' } ]" :key="metric.label" class="metric reveal" :style="{ '--reveal-delay': `${index * 35}ms` }"><p>{{ metric.label }}</p><strong>{{ metric.value }}</strong><div><span class="metric-trend up">{{ metric.trend }}</span><span>{{ metric.note }}</span></div></article></section>
        <section class="ticket-grid"><article class="panel ticket-list-panel reveal"><div class="panel-heading"><div><h2>最近人工咨询</h2></div><span class="status warning"><i></i>{{ serviceTickets.filter(item => item.status !== '已解决').length }} 进行中</span></div><button v-for="ticket in serviceTickets" :key="ticket.id" class="ticket-row" :class="{ active: selectedTicketId === ticket.id }" type="button" @click="selectedTicketId = ticket.id"><span class="ticket-user">{{ ticket.user.slice(0, 1) }}</span><span class="ticket-copy"><strong>{{ ticket.question }}</strong><small>{{ ticket.id }} · {{ ticket.time }}</small></span><span class="status" :class="{ warning: ticket.status !== '已解决' }"><i></i>{{ ticket.status }}</span></button></article><aside v-if="selectedTicket" class="panel ticket-detail reveal" style="--reveal-delay: 60ms"><div class="panel-heading"><div><h2>{{ selectedTicket.category }}</h2></div><span class="status" :class="{ warning: selectedTicket.status !== '已解决' }"><i></i>{{ selectedTicket.status }}</span></div><div class="ticket-question"><span>用户问题</span><strong>{{ selectedTicket.question }}</strong></div><dl class="ticket-context"><div><dt>用户与来源</dt><dd>{{ selectedTicket.user }} · {{ selectedTicket.source }}</dd></div><div><dt>用户上下文</dt><dd>{{ selectedTicket.context }}</dd></div><div><dt>RAG 检索记录</dt><dd>{{ selectedTicket.retrieval }}</dd></div><div><dt>当前处理人</dt><dd>{{ selectedTicket.handler }}</dd></div></dl><div class="ticket-actions"><button v-if="selectedTicket.status === '待分配'" class="refresh-button" type="button" @click="assignTicket(selectedTicket)">分配给我</button><button v-if="selectedTicket.status !== '已解决'" class="subtle-button" type="button" @click="resolveTicket(selectedTicket)">标记已解决</button><button class="subtle-button" type="button" @click="navigate('admin', 'feedback')">查看关联知识缺口</button></div></aside></section>
      </template>

      <template v-else-if="isGrowthTool">
        <section class="page-heading generic-heading"><div><h1>{{ page.label }}</h1></div><button class="refresh-button" type="button" @click="flash(`${page.label}配置已保存`)"><span>+</span>{{ pageKey === "automation" ? "新建流程" : pageKey === "rules" ? "新建规则" : "刷新数据" }}</button></section>

        <template v-if="pageKey === 'funnel'">
          <section class="metric-grid"><article v-for="(metric, index) in [{ label: '广告到达', value: '5,204', trend: '较上期 +6.8%', note: '有效到达率 82.4%' }, { label: '注册转化', value: '24.7%', trend: '较上期 +1.9%', note: '1,286 位新用户' }, { label: '首单转化', value: '33.3%', trend: '较上期 +11.2%', note: '428 笔首单' }, { label: '7 日复购', value: '18.6%', trend: '较上期 +2.1%', note: '239 位复购用户' }]" :key="metric.label" class="metric reveal" :style="{ '--reveal-delay': `${index * 35}ms` }"><p>{{ metric.label }}</p><strong>{{ metric.value }}</strong><div><span class="metric-trend up">{{ metric.trend }}</span><span>{{ metric.note }}</span></div></article></section>
          <section class="tool-grid funnel-grid"><article class="panel tool-panel reveal"><div class="panel-heading"><div><h2>从广告到复购</h2></div><label class="select-wrap"><select v-model="funnelSource"><option>全部广告来源</option><option>信息流广告</option><option>搜索广告</option></select></label></div><p class="filter-note">{{ funnelSource }}</p><div class="funnel-list"><div v-for="(stage, index) in funnelStages" :key="stage.name" class="funnel-row"><div class="funnel-meta"><span>{{ index + 1 }}</span><strong>{{ stage.name }}</strong><small>{{ stage.note }}</small></div><div class="funnel-bar"><i :style="{ width: `${[100, 76, 59, 32, 18][index]}%` }"></i></div><b>{{ stage.value }}</b><em>{{ stage.rate }}</em></div></div></article><article class="panel tool-panel reveal"><div class="panel-heading"><div><h2>流失节点</h2></div></div><div class="diagnosis-list"><div><strong>注册 → 领券</strong><p>300 位用户未领券。</p><button type="button" @click="navigate('admin', 'automation')">配置自动化 →</button></div><div><strong>领券 → 首单</strong><p>领券 20 小时未下单。</p><button type="button" @click="navigate('admin', 'segments')">查看用户分群 →</button></div></div></article></section>
        </template>

        <template v-else-if="pageKey === 'segments'">
          <section class="tool-grid segment-grid"><article class="panel tool-panel reveal"><div class="panel-heading"><div><h2>可运营用户分群</h2></div><button class="subtle-button" type="button" @click="flash('已创建分群草稿')">新建 <span>+</span></button></div><div class="segment-list"><button v-for="segment in segments" :key="segment.id" class="segment-item" :class="{ active: selectedSegmentId === segment.id }" type="button" @click="selectedSegmentId = segment.id"><span><strong>{{ segment.name }}</strong><small>{{ segment.rule }}</small></span><b>{{ segment.count }}</b></button></div></article><article class="panel tool-panel reveal"><div class="panel-heading"><div><h2>{{ selectedSegment.name }}</h2></div><span class="status"><i></i>实时计算</span></div><dl class="segment-detail"><div><dt>命中规则</dt><dd>{{ selectedSegment.rule }}</dd></div><div><dt>覆盖用户</dt><dd>{{ selectedSegment.count }} 位用户</dd></div><div><dt>运营策略</dt><dd>{{ selectedSegment.strategy }}</dd></div></dl><button class="refresh-button" type="button" @click="flash(`已为「${selectedSegment.name}」创建定向活动草稿`)">创建定向活动</button></article></section>
        </template>

        <template v-else-if="pageKey === 'automation'">
          <section class="panel tool-panel reveal"><div class="panel-heading"><div><h2>运营流程编排</h2></div><button class="subtle-button" type="button" @click="flash('已打开流程画布')">查看画布 <span>→</span></button></div><div class="flow-list"><article v-for="flow in automationFlows" :key="flow.id" class="flow-item"><div class="flow-state" :class="{ paused: !flow.enabled }">{{ flow.enabled ? "运行中" : "已暂停" }}</div><div><strong>{{ flow.name }}</strong><p>{{ flow.steps }}</p><small>当前覆盖 {{ flow.audience }} 位用户</small></div><button class="switch-button" :class="{ on: flow.enabled }" type="button" :aria-label="`${flow.enabled ? '暂停' : '启用'}${flow.name}`" @click="toggleFlow(flow.id)"><i></i></button></article></div></section>
        </template>

        <template v-else-if="pageKey === 'rules'">
          <section class="panel tool-panel reveal"><div class="panel-heading"><div><h2>营销规则引擎</h2></div><button class="subtle-button" type="button" @click="flash('规则版本已保存')">查看版本 <span>→</span></button></div><div class="rule-list"><article v-for="rule in marketingRules" :key="rule.id" class="rule-item"><div class="rule-dot" :class="{ disabled: !rule.enabled }"></div><div class="rule-copy"><strong>{{ rule.name }}</strong><p><span>满足：</span>{{ rule.condition }}</p><p><span>执行：</span>{{ rule.outcome }}</p></div><button class="subtle-button" type="button" @click="testRule(rule)">模拟命中</button><button class="switch-button" :class="{ on: rule.enabled }" type="button" :aria-label="`${rule.enabled ? '停用' : '启用'}${rule.name}`" @click="toggleRule(rule.id)"><i></i></button></article></div><p v-if="ruleTestResult" class="rule-result" role="status">{{ ruleTestResult }}</p></section>
        </template>

        <template v-else-if="pageKey === 'rewards'">
          <section class="metric-grid"><article v-for="(metric, index) in [{ label: '今日已发放', value: '2,342', trend: '成功率 99.4%', note: '券、积分、成长值' }, { label: '待审核奖励', value: '2', trend: '需要处理', note: '均命中风控规则' }, { label: '今日补发', value: '18', trend: '人工复核后发放', note: '发放成功率 100%' }, { label: '异常拦截', value: '36', trend: '较昨日 -8.1%', note: '避免奖励成本 ¥512' }]" :key="metric.label" class="metric reveal" :style="{ '--reveal-delay': `${index * 35}ms` }"><p>{{ metric.label }}</p><strong>{{ metric.value }}</strong><div><span class="metric-trend up">{{ metric.trend }}</span><span>{{ metric.note }}</span></div></article></section>
          <section class="panel tool-panel generic-table reveal"><div class="panel-heading"><div><h2>发放与风控审核</h2></div><label class="select-wrap"><select v-model="rewardStatus"><option>全部状态</option><option>已发放</option><option>待审核</option></select></label></div><div class="table-scroll"><table><thead><tr><th>流水号</th><th>用户</th><th>奖励</th><th>类型</th><th>发放时间</th><th>状态</th><th>风控结果</th><th>操作</th></tr></thead><tbody><tr v-for="record in visibleRewards" :key="record.id"><td>{{ record.id }}</td><td><strong>{{ record.user }}</strong></td><td>{{ record.reward }}</td><td>{{ record.type }}</td><td>{{ record.time }}</td><td><span class="status" :class="{ warning: record.status === '待审核' }"><i></i>{{ record.status }}</span></td><td>{{ record.risk }}</td><td><button v-if="record.status === '待审核'" class="subtle-button" type="button" @click="reviewReward(record.id)">审核通过</button><span v-else>—</span></td></tr></tbody></table></div></section>
        </template>
      </template>

      <template v-else>
        <section class="page-heading generic-heading"><div><h1>{{ page.label }}</h1></div><button class="refresh-button" type="button" @click="flash(`${page.label}的数据已刷新`)"><span>+</span>{{ portal === "admin" ? `配置${page.label}` : "查看规则" }}</button></section>
        <section class="metric-grid"><article v-for="(metric, index) in pageMetrics" :key="metric.label" class="metric reveal" :style="{ '--reveal-delay': `${index * 38}ms` }"><p>{{ metric.label }}</p><strong>{{ metric.value }}</strong><div><span class="metric-trend up">{{ metric.trend }}</span><span>{{ metric.note }}</span></div></article></section>
        <section class="workspace-grid"><article class="panel workspace-main reveal" style="--reveal-delay: 130ms"><div class="panel-heading"><div><h2>{{ page.label }}队列</h2></div><button class="subtle-button" type="button" @click="flash('筛选已应用')">筛选 <span>⌄</span></button></div><div class="workspace-list"><div v-for="row in rows" :key="row.name" class="workspace-row"><div><strong>{{ row.name }}</strong><span>{{ portal === "admin" ? `负责人：${row.owner}` : "我的任务" }} · {{ row.time }}</span></div><span class="status" :class="{ warning: row.status === '需关注' }"><i></i>{{ row.status }}</span></div></div></article><article class="panel workspace-side reveal" style="--reveal-delay: 175ms"><div class="panel-heading"><div><h2>{{ portal === "admin" ? "操作" : "我的操作" }}</h2></div></div><button v-for="action in actions" :key="action" class="quick-action" type="button" @click="flash(`${action}已创建草稿`)"><span>{{ action }}</span><b>→</b></button></article></section>
        <section class="panel generic-table reveal" style="--reveal-delay: 215ms"><div class="panel-heading"><div><h2>{{ page.label }}记录</h2></div><button class="subtle-button" type="button" @click="flash('已打开完整列表')">查看全部 <span>→</span></button></div><div class="table-scroll"><table><thead><tr><th>名称</th><th>负责人</th><th>最近更新</th><th>状态</th></tr></thead><tbody><tr v-for="row in rows" :key="`table-${row.name}`"><td><strong>{{ row.name }}</strong></td><td>{{ row.owner }}</td><td>{{ row.time }}</td><td><span class="status" :class="{ warning: row.status === '需关注' }"><i></i>{{ row.status }}</span></td></tr></tbody></table></div></section>
      </template>
    </main>

    <button class="support-fab" type="button" aria-label="打开智能客服" @click="navigate(portal, 'service')"><svg viewBox="0 0 24 24" aria-hidden="true"><path d="M5.5 17.5 3.8 21l4.2-1.4A8.3 8.3 0 0 0 12 20.5c4.7 0 8.5-3.4 8.5-7.6S16.7 5.3 12 5.3 3.5 8.7 3.5 12.9c0 1.7.6 3.3 2 4.6Z" /><path d="M8.3 12.5h.1m3.5 0h.1m3.5 0h.1" /></svg><span>客服</span></button>

    <Transition name="drawer"><div v-if="mobileNavOpen" class="mobile-nav-layer" @click.self="mobileNavOpen = false"><aside class="mobile-nav"><div class="mobile-nav-header"><button class="brand brand-button" type="button" @click="navigate(portal, portal === 'admin' ? 'dashboard' : 'home')"><span class="brand-mark">A</span><span>AdGrowth</span></button><button type="button" @click="mobileNavOpen = false">×</button></div><div class="portal-switcher"><button type="button" :class="{ active: portal === 'admin' }" @click="navigate('admin', 'dashboard')">管理端</button><button type="button" :class="{ active: portal === 'client' }" @click="navigate('client', 'home')">用户端</button></div><p class="workspace-label">{{ portal === "admin" ? "工作台" : "功能导航" }}</p><nav v-if="portal === 'admin'" class="nav-menu"><section v-for="group in adminNavGroups" :key="`m-${group.key}`" class="nav-group" :class="{ active: group.items.some(item => item.key === pageKey) }"><button class="nav-group-toggle" type="button" :aria-expanded="group.open" @click="toggleNavGroup(group.key)"><span>{{ group.label }}</span><small>{{ group.items.length }}</small><b :class="{ open: group.open }">⌄</b></button><div v-show="group.open" class="nav-group-items"><button v-for="item in group.items" :key="`m-${item.key}`" class="nav-item" :class="{ active: pageKey === item.key }" type="button" @click="navigate(portal, item.key)"><span class="nav-icon">{{ item.icon }}</span><span>{{ item.label }}</span></button></div></section></nav><nav v-else><button v-for="item in currentNav" :key="`m-${item.key}`" class="nav-item" :class="{ active: pageKey === item.key }" type="button" @click="navigate(portal, item.key)"><span class="nav-icon">{{ item.icon }}</span><span>{{ item.label }}</span></button></nav></aside></div></Transition>
    <Transition name="toast"><p v-if="notice" class="toast" role="status">{{ notice }}</p></Transition>
  </div>
</template>
