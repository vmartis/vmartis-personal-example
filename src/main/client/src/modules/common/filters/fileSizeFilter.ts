export default (value: any) => {
  let size = Math.round(value / 1024)
  let unit = 'KB'
  if (size > 1024) {
    size = Math.round(size / 1024)
    unit = 'MB'
  }
  return value ? size + ' ' + unit : null
}
