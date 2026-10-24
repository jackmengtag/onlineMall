/**
 * 跨境电商金额工具。
 * 后端金额统一使用最小货币单位的 Long 存储，前端展示时按 100 换算。
 */

const CURRENCY_SYMBOLS = {
  CNY: '¥',
  USD: '$',
  EUR: '€',
  GBP: '£',
  JPY: 'JP¥',
  HKD: 'HK$',
  AUD: 'A$',
  CAD: 'C$',
  SGD: 'S$'
}

/**
 * 最小货币单位转主单位（Long -> Number）
 */
export function fromMinor(amount) {
  if (amount === null || amount === undefined || amount === '') {
    return 0
  }
  const value = Number(amount)
  return isNaN(value) ? 0 : value / 100
}

/**
 * 主单位转最小货币单位（Number -> Long）
 */
export function toMinor(amount) {
  if (amount === null || amount === undefined || amount === '') {
    return 0
  }
  const value = Number(amount)
  return isNaN(value) ? 0 : Math.round(value * 100)
}

/**
 * 格式化金额（入参为最小货币单位）
 */
export function formatMoney(amount, currencyCode = 'CNY') {
  const value = fromMinor(amount)
  const symbol = CURRENCY_SYMBOLS[currencyCode] || ''
  const text = value.toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })
  return symbol + text
}
