export default class EntityFetchParams {
  force: boolean
  parameters: any

  constructor (force: boolean, parameters?: any) {
    this.force = force
    this.parameters = parameters
  }
}
