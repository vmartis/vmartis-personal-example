// hook for cleaning damaged url e.g. with fbclid from facebook chat: https://alfa-acc.smartbrains.cz/?fbclid=IwAR2tLpSv5cQgChp2EWRoFa877WkF00hdLhm2saQGEX-M4HfC_wi7LTgY-Gk#/login?redirectTo=%2Fhome
export default () => {
  if (window.location.search) {
    window.location.assign(window.location.origin + window.location.pathname + window.location.hash)
    return true
  }
  return false
}
