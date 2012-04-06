class Manifest:
    def __init__(self, **kw):
        for k in kw:
            setattr(self, k, kw[k])

# Keys used in the semi-persistent token states:
m_TokenStates = Manifest(
    LAST_LOCATION = "LAST_LOCATION"
)
