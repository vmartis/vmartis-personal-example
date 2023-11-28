<template>
  <div class="content-wrapper">
    <div class="content-header">
      <b-card :bg-variant="detail ? '' : 'transparent'" :border-variant="detail ? '' : 'transparent'" :no-body="!detail" :class="{ 'card-primary card-outline' : detail }">
        <div class="row">
          <div class="col-md-6 col-sm-12">
            <h1 v-if="translateTitle">{{ title | translate }}</h1>
            <h1 v-else>{{ title }}</h1>
          </div>
          <div class="col-md-6 col-sm-12 text-right" v-if="breadCrumbData">
            <div class="row">
              <div class="col">
                <b-breadcrumb class="pt-1 float-right">
                  <b-breadcrumb-item to="home">
                    <i class="fas fa-home"></i>
                  </b-breadcrumb-item>
                  <b-breadcrumb-item v-for="breadCrumbLink in breadCrumbData.links" :active="!breadCrumbLink.routeName" v-bind:key="breadCrumbLink.textKey" :to="{ name: breadCrumbLink.routeName || '', params: breadCrumbLink.routeParams }">
                    <i :class="breadCrumbLink.icon" v-if="breadCrumbLink.icon"></i>
                    <span v-if="breadCrumbLink.textKey">{{ breadCrumbLink.textKey | translate }}</span>
                    <span v-else-if="breadCrumbLink.text">{{ breadCrumbLink.text }}</span>
                  </b-breadcrumb-item>
                </b-breadcrumb>
              </div>
              <div class="col-auto pl-0">
                <router-link v-if="detail" tag="button" class="btn btn-link btn-tool pl-0 close-button" :to="{ name: breadCrumbData.closeRoute }"><i class="fas fa-times text-primary fa-2x"></i></router-link>
              </div>
            </div>
          </div>

        </div>
      </b-card>
    </div>
    <div class="content">
      <div class="row">
        <div class="col-12">
          <slot></slot>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import _ from 'lodash'
import BreadCrumbData from '@/modules/common/components/breadcrumb/breadcrumbData'
import Component from 'vue-class-component'
import { Prop } from 'vue-property-decorator'
import Vue from 'vue'

@Component
export default class Page extends Vue {
  @Prop({ type: String, required: true }) title!: string
  @Prop({ type: Object, required: false }) breadCrumbData?: BreadCrumbData
  @Prop({ type: Boolean, default: true }) translateTitle!: boolean

  get detail () {
    return this.breadCrumbData && !_.isNil(this.breadCrumbData.closeRoute)
  }
}
</script>
