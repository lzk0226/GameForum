<!-- ============================================ -->
<!-- Item.vue -->
<script>
export default {
  name: 'MenuItem',
  functional: true,
  props: {
    icon: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    }
  },
  render(h, context) {
    const { icon, title } = context.props
    const vnodes = []

    if (icon) {
      vnodes.push(<svg-icon icon-class={icon} style="margin-right: 8px; filter: drop-shadow(0 1px 2px rgba(0, 0, 0, 0.3));"/>)
    }

    if (title) {
      const titleStyle = {
        textShadow: '0 1px 2px rgba(0, 0, 0, 0.3)',
        fontWeight: '500'
      }

      if (title.length > 5) {
        vnodes.push(<span slot='title' title={(title)} style={titleStyle}>{(title)}</span>)
      } else {
        vnodes.push(<span slot='title' style={titleStyle}>{(title)}</span>)
      }
    }
    return vnodes
  }
}
</script>
